package org.jtwig.parser.parboiled.base;

import org.jtwig.parser.parboiled.AbstractParserTest;
import org.jtwig.parser.parboiled.model.LimitProperties;
import org.junit.Test;
import org.parboiled.support.ParsingResult;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;

public class LimitsParserTest extends AbstractParserTest {
    private LimitsParser underTest = context.parser(LimitsParser.class);

    @Test
    public void registered() throws Exception {
        assertNotNull(context.parser(LimitsParser.class));
    }

    @Test
    public void startCommentWithoutDash() throws Exception {
        ParsingResult<LimitProperties> parse = parse(underTest.startComment(), "{#");

        assertThat(parse.matched, is(true));
        assertThat(underTest.lastWhiteSpace(), is(false));
    }

    @Test
    public void startCommentWithDash() throws Exception {
        ParsingResult<LimitProperties> parse = parse(underTest.startComment(), "{#-");

        assertThat(parse.matched, is(true));
        assertThat(underTest.lastWhiteSpace(), is(true));
    }

    @Test
    public void endCommentWithoutDash() throws Exception {
        ParsingResult<LimitProperties> parse = parse(underTest.endComment(), "#}");

        assertThat(parse.matched, is(true));
        assertThat(underTest.lastWhiteSpace(), is(false));
    }

    @Test
    public void endCommentWithDash() throws Exception {
        ParsingResult<LimitProperties> parse = parse(underTest.endComment(), "-#}");

        assertThat(parse.matched, is(true));
        assertThat(underTest.lastWhiteSpace(), is(true));
    }
}