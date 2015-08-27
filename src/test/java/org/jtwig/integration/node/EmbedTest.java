package org.jtwig.integration.node;

import com.google.common.base.Optional;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.integration.AbstractIntegrationTest;
import org.jtwig.parser.ParseException;
import org.jtwig.resource.Resource;
import org.jtwig.resource.StringResource;
import org.jtwig.resource.exceptions.ResourceNotFoundException;
import org.jtwig.resource.resolver.ResourceResolver;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.jtwig.environment.EnvironmentConfigurationBuilder.configuration;

public class EmbedTest extends AbstractIntegrationTest {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void simpleEmbed() throws Exception {
        JtwigTemplate template = JtwigTemplate.inlineTemplate("{% embed 'a' %}{% block one %}Ola{% endblock %}{% endembed %}", configuration()
                .resources().withResourceResolver(resolvePath("a", "{% block one %}three{% endblock %}")).and()
                .build());
        String result = template.render(JtwigModel.newModel());
        assertThat(result, is("Ola"));
    }

    @Test
    public void embedResourceNotFound() throws Exception {
        expectedException.expect(ResourceNotFoundException.class);
        expectedException.expectMessage(containsString("Resource 'one' not found"));

        JtwigTemplate.inlineTemplate("{% embed 'one' %}{% endembed %}")
                .render(JtwigModel.newModel());
    }

    @Test
    public void embedResourceNotFoundIgnoreMissing() throws Exception {
        String result = JtwigTemplate.inlineTemplate("{% embed 'one' ignore missing %}{% endembed %}")
                .render(JtwigModel.newModel());

        assertThat(result, is(""));
    }

    @Test
    public void simpleEmbedWithComment() throws Exception {
        JtwigTemplate template = JtwigTemplate.inlineTemplate("{% embed 'classpath:/example/extends/extendable-template.twig' %}{# test #}{% block one %}Ola{% endblock %}{% endembed %}");
        String result = template.render(JtwigModel.newModel());
        assertThat(result, is("Ola"));
    }

    @Test
    public void embedWithInvalidInnerElement() throws Exception {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(containsString("Embed construct can only contain block elements"));

        JtwigTemplate.inlineTemplate("{% embed 'classpath:/example/extends/extendable-template.twig' %}{% if (true) %}{% endif %}{% endembed %}")
                .render(JtwigModel.newModel());
    }

    @Test
    public void embedWithoutExpression() throws Exception {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(containsString("Embed construct missing path expression"));

        JtwigTemplate.inlineTemplate("{% embed %}{% endembed %}")
                .render(JtwigModel.newModel());
    }

    @Test
    public void embedMissingIgnoreMissing() throws Exception {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(containsString("Did you mean 'ignore missing'?"));

        JtwigTemplate.inlineTemplate("{% embed 'asdasd' ignore %}{% endembed %}")
                .render(JtwigModel.newModel());
    }

    @Test
    public void embedMissingEndCode() throws Exception {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(containsString("Code island not closed"));

        JtwigTemplate.inlineTemplate("{% embed 'asdasd' {% endembed %}")
                .render(JtwigModel.newModel());
    }

    @Test
    public void embedMissingEndEmbeded() throws Exception {
        expectedException.expect(ParseException.class);
        expectedException.expectMessage(containsString("Embed construct can only contain block elements. You might be missing the endembed tag."));

        JtwigTemplate.inlineTemplate("{% embed 'asdasd' %}")
                .render(JtwigModel.newModel());
    }

    private ResourceResolver resolvePath(final String path, final String content) {
        return new ResourceResolver() {
            @Override
            public Optional<Resource> resolve(Resource resource, String relativePath) {
                if (path.equals(relativePath)) {
                    return Optional.<Resource>of(new StringResource(content));
                }
                return Optional.absent();
            }
        };
    }
}
