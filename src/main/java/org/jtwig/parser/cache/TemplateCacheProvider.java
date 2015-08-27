package org.jtwig.parser.cache;

import com.google.common.cache.Cache;
import org.jtwig.model.tree.Node;
import org.jtwig.resource.Resource;

public interface TemplateCacheProvider {
    public Cache<Resource, Node> cache ();
}
