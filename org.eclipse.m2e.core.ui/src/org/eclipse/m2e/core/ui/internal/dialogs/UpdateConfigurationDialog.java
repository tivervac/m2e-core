/*******************************************************************************
 * Copyright (c) 2010 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.m2e.core.ui.internal.dialogs;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.widgets.Shell;

import org.eclipse.m2e.core.ui.internal.Messages;

/**
 * Update Maven Project Configuration Dialog
 *
 * @author Fred Bricon
 */
public class UpdateConfigurationDialog extends SelectMavenProjectsDialog {

  /**
   * @param parentShell
   * @param initialSelection
   */
  public UpdateConfigurationDialog(Shell parentShell, IProject[] initialSelection) {
    super(parentShell, initialSelection, Messages.UpdateConfigurationDialog_title, Messages.UpdateConfigurationDialog_dialogMessage);
  }

}