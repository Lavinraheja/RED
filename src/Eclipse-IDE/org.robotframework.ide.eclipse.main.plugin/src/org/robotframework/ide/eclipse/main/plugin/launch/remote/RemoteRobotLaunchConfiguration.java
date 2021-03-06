/*
 * Copyright 2017 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.launch.remote;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.robotframework.ide.eclipse.main.plugin.launch.AbstractRobotLaunchConfiguration;

public class RemoteRobotLaunchConfiguration extends AbstractRobotLaunchConfiguration {

    public static final String TYPE_ID = "org.robotframework.ide.remoteRobotLaunchConfiguration";

    public static final String CURRENT_CONFIGURATION_VERSION = "1";

    public RemoteRobotLaunchConfiguration(final ILaunchConfiguration config) {
        super(config);
    }

    @Override
    public boolean isUsingRemoteAgent() throws CoreException {
        return true;
    }

    @Override
    public void setCurrentConfigurationVersion() throws CoreException {
        final ILaunchConfigurationWorkingCopy launchCopy = asWorkingCopy();
        launchCopy.setAttribute(VERSION_OF_CONFIGURATION, CURRENT_CONFIGURATION_VERSION);
    }

    @Override
    public boolean hasValidVersion() throws CoreException {
        return CURRENT_CONFIGURATION_VERSION.equals(getConfigurationVersion());
    }

    @Override
    public String getCurrentConfigurationVersion() throws CoreException {
        return CURRENT_CONFIGURATION_VERSION;
    }

    public static ILaunchConfigurationWorkingCopy prepareDefault(final IProject project) throws CoreException {
        final ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
        final String namePrefix = project.getName();
        final String name = launchManager.generateLaunchConfigurationName(namePrefix);

        final ILaunchConfigurationWorkingCopy configuration = launchManager.getLaunchConfigurationType(TYPE_ID)
                .newInstance(null, name);
        final RemoteRobotLaunchConfiguration remoteConfig = new RemoteRobotLaunchConfiguration(configuration);
        remoteConfig.fillDefaults();
        remoteConfig.setProjectName(namePrefix);
        return configuration;
    }

}
