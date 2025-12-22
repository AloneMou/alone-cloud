package com.alone.coder.radius.tinyradius.core.dictionary.parser;

import com.alone.coder.radius.tinyradius.core.dictionary.WritableDictionary;
import com.alone.coder.radius.tinyradius.core.dictionary.parser.resolver.ClasspathResourceResolver;
import com.alone.coder.radius.tinyradius.core.dictionary.parser.resolver.FileResourceResolver;
import com.alone.coder.radius.tinyradius.core.dictionary.parser.resolver.ResourceResolver;

import java.io.IOException;

/**
 * Parses a dictionary in Radiator format and fills a WritableDictionary.
 */
public record DictionaryParser(ResourceResolver resourceResolver) {

    public static DictionaryParser newClasspathParser() {
        return new DictionaryParser(ClasspathResourceResolver.INSTANCE);
    }

    public static DictionaryParser newFileParser() {
        return new DictionaryParser(FileResourceResolver.INSTANCE);
    }

    /**
     * Returns a new dictionary filled with the contents
     * from the given input stream.
     *
     * @param resource location of resource, resolved depending on {@link ResourceResolver}
     * @return dictionary object
     * @throws IOException parse error reading from input
     */
    public WritableDictionary parseDictionary(String resource) throws IOException {
        final ResourceParser resourceParser = new ResourceParser(resourceResolver);
        return resourceParser.parseDictionary(resource);
    }
}
