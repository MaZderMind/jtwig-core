package org.jtwig.parser.parboiled.base;

import org.jtwig.model.tree.TextNode;
import org.jtwig.parser.parboiled.ParserContext;
import org.jtwig.parser.parboiled.model.LimitProperties;
import org.parboiled.Rule;
import org.parboiled.annotations.Label;

public class LimitsParser extends BasicParser<LimitProperties> {
    boolean whiteSpace = false;
    TextNode update = null;

    public LimitsParser(ParserContext context) {
        super(LimitsParser.class, context);
    }

    public Rule startComment () {
        return Sequence(
                parserContext().syntaxConfiguration().startComment(),
                LimitProperty(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    public Rule endComment () {
        return Sequence(
                LimitProperty(),
                parserContext().syntaxConfiguration().endComment(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    public Rule startOutput () {
        return Sequence(
                parserContext().syntaxConfiguration().startOutput(),
                LimitProperty(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    public Rule endOutput () {
        return Sequence(
                LimitProperty(),
                parserContext().syntaxConfiguration().endOutput(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    @Label("Start Code Island")
    public Rule startCode () {
        return Sequence(
                parserContext().syntaxConfiguration().startCode(),
                LimitProperty(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    @Label("End Code Island")
    public Rule endCode () {
        return Sequence(
                LimitProperty(),
                parserContext().syntaxConfiguration().endCode(),
                markWhiteSpace(pop().isWhiteSpaceControl())
        );
    }

    Rule LimitProperty() {
        return FirstOf(
                Sequence(
                        String("-"),
                        push(new LimitProperties(true))
                ),
                push(new LimitProperties(false))
        );
    }

    boolean markWhiteSpace(boolean whiteSpace) {
        if (!getContext().inPredicate()) {
            if (update != null) {
                update.trimRight(whiteSpace);
                update = null;
            }
            this.whiteSpace = whiteSpace;
        }
        return true;
    }

    public Rule anyEnd() {
        return FirstOf(
                parserContext().syntaxConfiguration().startComment(),
                parserContext().syntaxConfiguration().startCode(),
                parserContext().syntaxConfiguration().startOutput()
        );
    }

    public boolean lastWhiteSpace() {
        return whiteSpace;
    }

    public boolean update(TextNode update) {
        this.update = update;
        return true;
    }
}
