package dojo;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 *
 * Programming paradigm: https://en.wikipedia.org/wiki/Programming_paradigm
 * imperative - procedural
 * object oriented
 * fonctional
 */
public class ProgrammingParadigmTest {

    public static Stream<Paradigm> paradigms() {
        return Stream.of(
                new ProceduralParadigm(),
                new ObjectParadigm(),
                new FonctionalParadigm());
    }

    @ParameterizedTest(name = "Paradigm \"{0}\"")
    @MethodSource("paradigms")
    public void test_one_paradigm(Paradigm paradigm) {

        List<String> myList = Arrays.asList("toto", "bob", "titi");
        String result = paradigm.transform(myList);
        assertEquals("* toto\n* BOB\n* titi", result);
    }


    interface Paradigm {
        String transform(List<String> myList);
    }

    static class ProceduralParadigm implements Paradigm {
        
        public String transform(List<String> myList) {

            boolean isAsciidoctor = true;
            return transform(myList, isAsciidoctor);
        }

        private String transform(List<String> myList, boolean isAsciidoctor) {
            String result = "";
            String separator = "";

            for (String value : myList) {
                String formattedValue = getFormattedValue(value);

                result += separator;

                if (isAsciidoctor) {
                    result += "* " + formattedValue;
                } else {
                    result += "- " + formattedValue;
                }

                separator = "\n";
            }

            return result;
        }

        private String getFormattedValue(String value) {
            if ("bob".equals(value)) {
                return "BOB";
            } else {
                return value;
            }
        }
    }

    static class ObjectParadigm implements Paradigm {

        public String transform(List<String> myList) {

            final AsciidocOutputFormat outputFormat = new AsciidocOutputFormat();

            return transform(myList, outputFormat);
        }

        private String transform(List<String> myList, AsciidocOutputFormat outputFormat) {
            String result = "";
            String separator = "";

            for (String value : myList) {
                FormatName nameFormatter = getNameFormatter(value);
                final String formattedName = nameFormatter.format(value);

                result += separator;
                result += outputFormat.formatList(formattedName);
                separator = "\n";
            }

            return result;
        }

        private FormatName getNameFormatter(String value) {
            FormatName nameFormatter = null;
            if ("bob".equals(value)) {
                nameFormatter = new BobFormat();
            } else {
                nameFormatter = new StandardFormat();
            }
            return nameFormatter;
        }


        interface FormatName {
            String format(String value);
        }

        class BobFormat implements FormatName {
            public String format(String value) {
                return "BOB";
            }
        }

        class StandardFormat implements FormatName {
            public String format(String value) {
                return value;
            }
        }

        interface OutputFormat {
            String formatList(String value);
        }

        class AsciidocOutputFormat implements OutputFormat {
            public String formatList(String value) {
                return "* " + value;
            }
        }

        class MarkdownOutputFormat implements OutputFormat {
            public String formatList(String value) {
                return "- " + value;
            }
        }

    }

    static class FonctionalParadigm implements Paradigm {
        
        public String transform(List<String> myList) {
            return transform(myList, this::formatListAsciiDoc);
        }

        private String transform(List<String> myList, Function<String, String> formatListFunction) {
            return myList.stream()
                .map(this::formatName)
                .map(formatListFunction)
                .collect(Collectors.joining("\n"));
        }

        private String formatName(String param){
            return "bob".equals(param)
                ?"BOB"
                :param;
        }

        private String formatListAsciiDoc(String param){
            return "* "+param;
        }

        private String formatListMarkdown(String param){
            return "- "+param;
        }
    }
}