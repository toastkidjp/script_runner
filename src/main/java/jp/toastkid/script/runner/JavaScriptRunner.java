/*
 * Copyright (c) 2017 toastkidjp.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompany this distribution.
 * The Eclipse Public License is available at http://www.eclipse.org/legal/epl-v10.html.
 */
package jp.toastkid.script.runner;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Optional;

import javax.script.ScriptContext;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.control.CompilationFailedException;
import org.fxmisc.richtext.CodeArea;

import jp.toastkid.script.highlight.Highlighter;
import jp.toastkid.script.highlight.JavaScriptHighlighter;

/**
 * JavaScript's script runner.
 * @author Toast kid
 *
 */
public class JavaScriptRunner extends ScriptRunner {

    /**
     * init ScriptEngine.
     */
    public JavaScriptRunner() {
        engine = new ScriptEngineManager().getEngineByName("javascript");
    }

    @Override
    public Optional<String> run(final String script) {

        if (StringUtils.isEmpty(script)) {
            return Optional.empty();
        }
        final StringBuilder result = new StringBuilder();

        try (final StringWriter writer = new StringWriter();) {
            final ScriptContext context = engine.getContext();
            context.setWriter(writer);
            context.setErrorWriter(writer);

            final java.lang.Object run = engine.eval(script);
            result.append(writer.toString()).append(LINE_SEPARATOR);
            if (run != null) {
                result.append("return = ").append(run.toString());
            }
            writer.close();
        } catch (final CompilationFailedException | IOException | ScriptException e) {
            e.printStackTrace();
            result.append("Occurred Exception.").append(LINE_SEPARATOR).append(e.getMessage());
        }
        return Optional.of(result.toString());
    }

    @Override
    public Highlighter initHighlight(final CodeArea codeArea) {
        return new JavaScriptHighlighter(codeArea);
    }
}
