package uk.co.malbec.welcometohell.ui.renderers;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.malbec.welcometohell.ui.Renderer;
import uk.co.malbec.welcometohell.ui.RenderingEngine;
import uk.co.malbec.welcometohell.wizard.domain.Option;
import uk.co.malbec.welcometohell.wizard.domain.Options;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateClasses;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateContent;
import static uk.co.malbec.welcometohell.ui.RendererUtilities.generateStyles;

@Component
public class OptionsRenderer implements Renderer<Options> {

    @Autowired
    RenderingEngine renderingEngine;

    @Override
    public boolean accepts(Class<?> clz) {
        return Options.class.equals(clz);
    }

    @Override
    public void render(VelocityEngine velocityEngine, String wizardSessionId, String pageId, Options element, StringWriter content, Map<String, Object> data) {

        VelocityContext context = new VelocityContext();


        generateClasses(element, context,"","text");
        generateStyles(element, context);

        generateContent(wizardSessionId, pageId, renderingEngine, element, data, context);


        velocityEngine.getTemplate("views/options.html").merge(context, content);
    }
}