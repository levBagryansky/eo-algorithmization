/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.algorithmize;

import com.jcabi.manifests.Manifests;
import picocli.CommandLine;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Main entrance.
 *
 * @since 0.0.1
 */
@CommandLine.Command(
    name = "accelerate",
    mixinStandardHelpOptions = true,
    description = "Optimizes eo code using rust inserts",
    versionProvider = Main.Version.class
)
public final class Main implements Callable<Integer> {

    /**
     * Flag indicating whether the input file is EO-program.
     */
    @CommandLine.Option(names = { "--from" },
        defaultValue = "xmir",
        description = "treat input file as EO program (not XMIR)")
    private String format;

    /**
     * Flag indicating whether the input file is EO-program.
     */
    @CommandLine.Option(names = { "--dest" },
        defaultValue = "target/generated",
        description = "treat input file as EO program (not XMIR)")
    private File dest;


    /**
     * Absolute path to input file.
     */
    @CommandLine.Parameters(index = "0",
        description = "Absolute path of file to transform")
    private File src;

    @Override
    public Integer call() throws IOException {
        new Algothmize(this.src.toPath(), this.dest.toPath()).exec();
        return 0;
    }

    /**
     * Main entrance for Java command line.
     * @param args The args from the command line.
     */
    public static void main(final String[] args) {
        new CommandLine(new Main()).execute(args);
    }

    /**
     * Version.
     * @since 0.0.2
     */
    static final class Version implements CommandLine.IVersionProvider {
        @Override
        public String[] getVersion() {
            return new String[]{
                String.format(
                    "EO-Algorithmization version is %s\nEO version is %s",
                    Manifests.read("Dejump-Version"),
                    Manifests.read("EO-Version")
                ),
            };
        }
    }

}
