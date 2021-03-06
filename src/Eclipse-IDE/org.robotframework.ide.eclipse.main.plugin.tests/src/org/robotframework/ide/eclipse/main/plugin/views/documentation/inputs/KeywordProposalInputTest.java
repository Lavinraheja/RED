/*
 * Copyright 2018 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.views.documentation.inputs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.robotframework.red.junit.jupiter.ProjectExtension.createFile;
import static org.robotframework.red.junit.jupiter.ProjectExtension.getFile;

import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.rf.ide.core.environment.IRuntimeEnvironment;
import org.rf.ide.core.libraries.Documentation.DocFormat;
import org.robotframework.ide.eclipse.main.plugin.model.RobotCasesSection;
import org.robotframework.ide.eclipse.main.plugin.model.RobotKeywordCall;
import org.robotframework.ide.eclipse.main.plugin.model.RobotModel;
import org.robotframework.ide.eclipse.main.plugin.model.RobotProject;
import org.robotframework.ide.eclipse.main.plugin.project.editor.libraries.Libraries;
import org.robotframework.red.junit.jupiter.Project;
import org.robotframework.red.junit.jupiter.ProjectExtension;

@ExtendWith(ProjectExtension.class)
public class KeywordProposalInputTest {

    @Project
    static IProject project;

    private RobotModel model;

    @BeforeEach
    public void beforeTest() {
        model = new RobotModel();
        final RobotProject robotProject = model.createRobotProject(project);
        robotProject.setStandardLibraries(new HashMap<>());
        robotProject.setReferencedLibraries(Libraries.createRefLib("lib", "Lib Kw 1", "Lib Kw 2"));
    }

    @AfterEach
    public void afterTest() {
        model.createSuiteFile(getFile(project, "suite.robot")).dispose();
        model.createSuiteFile(getFile(project, "res.robot")).dispose();
    }

    @Test
    public void exceptionIsThrown_whenPreparingInputButKeywordIsNotFound() throws Exception {
        final RobotKeywordCall call = createCall("unknown keyword");
        final KeywordProposalInput input = new KeywordProposalInput(call, "unknown keyword");

        assertThatExceptionOfType(DocumentationInputGenerationException.class).isThrownBy(() -> input.prepare())
                .withMessage("Keyword 'unknown keyword' not found, nothing to display");
    }

    @Test
    public void properLibraryKeywordDocUriIsProvidedForInput() throws Exception {
        final RobotKeywordCall call = createCall("Lib Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Lib Kw 1");
        input.prepare(model);

        assertThat(input.getInputUri().toString())
                .isEqualTo("library:/KeywordProposalInputTest/lib/Lib%20Kw%201?show_doc=true");
    }

    @Test
    public void properUserKeywordDocUriIsProvidedForInput() throws Exception {
        final RobotKeywordCall call = createCall("Res Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Res Kw 1");
        input.prepare(model);

        final IFile resourceFile = getFile(project, "res.robot");

        assertThat(input.getInputUri().toString())
                .isEqualTo(resourceFile.getLocationURI().toString() + "?show_doc=true&keyword=Res%20Kw%201");
    }

    @Test
    public void theInputContainsGivenCall() throws Exception {
        final RobotKeywordCall call = createCall("Lib Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Lib Kw 1");

        assertThat(input.contains("Lib Kw 1")).isFalse();
        assertThat(input.contains(call.getSuiteFile())).isFalse();
        assertThat(input.contains(call)).isTrue();
    }

    @Test
    public void properHtmlIsReturned_forLibraryKeyword() throws Exception {
        final IRuntimeEnvironment env = mock(IRuntimeEnvironment.class);
        when(env.createHtmlDoc(any(String.class), eq(DocFormat.ROBOT))).thenReturn("doc");

        final RobotKeywordCall call = createCall("Lib Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Lib Kw 1");
        input.prepare(model);

        final String html = input.provideHtml(env);
        assertThat(html).contains("library:/KeywordProposalInputTest/lib/Lib%20Kw%201?show_source=true\">lib</a>");
        assertThat(html).contains("library:/KeywordProposalInputTest/lib?show_doc=true\">Documentation</a>");
        assertThat(html).contains("Arguments", "[]", "doc");
    }

    @Test
    public void properHtmlIsReturned_forUserKeyword() throws Exception {
        final IRuntimeEnvironment env = mock(IRuntimeEnvironment.class);
        when(env.createHtmlDoc(any(String.class), eq(DocFormat.ROBOT))).thenReturn("doc");

        final RobotKeywordCall call = createCall("Res Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Res Kw 1");
        input.prepare(model);

        final String fileUri = getFile(project, "res.robot").getLocationURI().toString();
        final String fileLabel = getFile(project, "res.robot").getFullPath().toString();

        final String html = input.provideHtml(env);
        assertThat(html).contains(fileUri + "?show_source=true&keyword=Res%20Kw%201\">" + fileLabel + "</a>");
        assertThat(html).contains(fileUri + "?show_doc=true&suite=\">Documentation</a>");
        assertThat(html).contains("Arguments", "[x, y]", "doc");
    }

    @Test
    public void properRawDocumentationIsReturned_forLibraryDefinition() throws Exception {
        final RobotKeywordCall call = createCall("Lib Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Lib Kw 1");
        input.prepare(model);

        final String raw = input.provideRawText();
        assertThat(raw).contains("Name: Lib Kw 1");
        assertThat(raw).contains("Source: Library (lib)");
        assertThat(raw).contains("Arguments: []");
    }

    @Test
    public void properRawDocumentationIsReturned_forUserDefinition() throws Exception {
        final RobotKeywordCall call = createCall("Res Kw 1");
        final KeywordProposalInput input = new KeywordProposalInput(call, "Res Kw 1");
        input.prepare(model);

        final String fileLabel = getFile(project, "res.robot").getFullPath().toString();

        final String raw = input.provideRawText();
        assertThat(raw).contains("Name: Res Kw 1");
        assertThat(raw).contains("Source: User defined (" + fileLabel + ")");
        assertThat(raw).contains("Arguments: [x, y]");
    }

    private RobotKeywordCall createCall(final String calledKeywordName) throws Exception {
        createFile(project, "res.robot",
                "*** Keywords ***",
                "Res Kw 1",
                "  [Arguments]  ${x}  ${y}",
                "Res Kw 2");
        final IFile file = createFile(project, "suite.robot",
                "*** Settings ***",
                "Library  lib",
                "Resource  res.robot",
                "*** Test Cases ***",
                "test",
                "  " + calledKeywordName + "  arg1  arg2");
        return model.createSuiteFile(file)
                .findSection(RobotCasesSection.class)
                .get()
                .getChildren()
                .get(0)
                .getChildren()
                .get(0);
    }
}
