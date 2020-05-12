/* Generated by camel build tools - do NOT edit this file! */
package org.apache.camel.component.mustache;

import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.spi.GeneratedPropertyConfigurer;
import org.apache.camel.spi.PropertyConfigurerGetter;
import org.apache.camel.util.CaseInsensitiveMap;
import org.apache.camel.support.component.PropertyConfigurerSupport;

/**
 * Generated by camel build tools - do NOT edit this file!
 */
@SuppressWarnings("unchecked")
public class MustacheEndpointConfigurer extends PropertyConfigurerSupport implements GeneratedPropertyConfigurer, PropertyConfigurerGetter {

    @Override
    public boolean configure(CamelContext camelContext, Object obj, String name, Object value, boolean ignoreCase) {
        MustacheEndpoint target = (MustacheEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "allowtemplatefromheader":
        case "allowTemplateFromHeader": target.setAllowTemplateFromHeader(property(camelContext, boolean.class, value)); return true;
        case "basicpropertybinding":
        case "basicPropertyBinding": target.setBasicPropertyBinding(property(camelContext, boolean.class, value)); return true;
        case "contentcache":
        case "contentCache": target.setContentCache(property(camelContext, boolean.class, value)); return true;
        case "encoding": target.setEncoding(property(camelContext, java.lang.String.class, value)); return true;
        case "enddelimiter":
        case "endDelimiter": target.setEndDelimiter(property(camelContext, java.lang.String.class, value)); return true;
        case "lazystartproducer":
        case "lazyStartProducer": target.setLazyStartProducer(property(camelContext, boolean.class, value)); return true;
        case "startdelimiter":
        case "startDelimiter": target.setStartDelimiter(property(camelContext, java.lang.String.class, value)); return true;
        case "synchronous": target.setSynchronous(property(camelContext, boolean.class, value)); return true;
        default: return false;
        }
    }

    @Override
    public Map<String, Object> getAllOptions(Object target) {
        Map<String, Object> answer = new CaseInsensitiveMap();
        answer.put("allowTemplateFromHeader", boolean.class);
        answer.put("basicPropertyBinding", boolean.class);
        answer.put("contentCache", boolean.class);
        answer.put("encoding", java.lang.String.class);
        answer.put("endDelimiter", java.lang.String.class);
        answer.put("lazyStartProducer", boolean.class);
        answer.put("startDelimiter", java.lang.String.class);
        answer.put("synchronous", boolean.class);
        return answer;
    }

    @Override
    public Object getOptionValue(Object obj, String name, boolean ignoreCase) {
        MustacheEndpoint target = (MustacheEndpoint) obj;
        switch (ignoreCase ? name.toLowerCase() : name) {
        case "allowtemplatefromheader":
        case "allowTemplateFromHeader": return target.isAllowTemplateFromHeader();
        case "basicpropertybinding":
        case "basicPropertyBinding": return target.isBasicPropertyBinding();
        case "contentcache":
        case "contentCache": return target.isContentCache();
        case "encoding": return target.getEncoding();
        case "enddelimiter":
        case "endDelimiter": return target.getEndDelimiter();
        case "lazystartproducer":
        case "lazyStartProducer": return target.isLazyStartProducer();
        case "startdelimiter":
        case "startDelimiter": return target.getStartDelimiter();
        case "synchronous": return target.isSynchronous();
        default: return null;
        }
    }
}
