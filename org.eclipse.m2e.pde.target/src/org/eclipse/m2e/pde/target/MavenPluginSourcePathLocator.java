package org.eclipse.m2e.pde.target;

import java.io.File;
import java.util.Collection;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.m2e.core.embedder.ArtifactKey;
import org.eclipse.m2e.core.internal.MavenArtifactIdentifier;
import org.eclipse.pde.core.IPluginSourcePathLocator;
import org.eclipse.pde.core.plugin.IPluginBase;
import org.eclipse.pde.core.target.ITargetDefinition;
import org.eclipse.pde.core.target.ITargetLocation;
import org.eclipse.pde.core.target.ITargetPlatformService;
import org.eclipse.pde.internal.core.target.TargetPlatformService;

/**
 * Look up sources of plugins in maven
 *
 */
public class MavenPluginSourcePathLocator implements IPluginSourcePathLocator {

	@Override
	public IPath locateSource(IPluginBase plugin) {
		String installLocation = plugin.getModel().getInstallLocation();
		if (installLocation != null) {
			File file = new File(installLocation);
			if (file.isFile()) {
				String ext = FilenameUtils.getExtension(installLocation);
				String baseName = FilenameUtils.getBaseName(installLocation);
				File localFile = new File(file.getParentFile(), baseName + "-sources." + ext);
				if (localFile.isFile()) {
					return new Path(localFile.getAbsolutePath());
				}
				ArtifactKey artifact = aquireFromTargetState(file);
				if (artifact == null) {
					Collection<ArtifactKey> identify = MavenArtifactIdentifier.identify(file);
					if (identify.size() == 1) {
						artifact = identify.iterator().next();
					}
				}
				java.nio.file.Path location = MavenArtifactIdentifier.resolveSourceLocation(artifact, null);
				if (location != null) {
					return new Path(location.toFile().getAbsolutePath());
				}
			}
		}
		return null;
	}

	private ArtifactKey aquireFromTargetState(File file) {
		ITargetPlatformService platformService = TargetPlatformService.getDefault();
		try {
			ITargetDefinition targetDefinition = platformService.getWorkspaceTargetDefinition();
			if (targetDefinition != null) {
				for (ITargetLocation location : targetDefinition.getTargetLocations()) {
					if (location instanceof MavenTargetLocation) {
						MavenTargetLocation targetLocation = (MavenTargetLocation) location;
						Artifact lookupArtifact = targetLocation.lookupArtifact(file);
						if (lookupArtifact != null) {
							return new ArtifactKey(lookupArtifact);
						}
					}
				}
			}
		} catch (CoreException e) {
			// target might not be ready...
		}
		return null;
	}

}
