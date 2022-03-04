package example.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import java.util.List;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_17)
@SupportedAnnotationTypes("example.base.LookAt")
public class TestProcessor extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);

        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING, "running processor for LookAt");
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element rootElement : roundEnv.getRootElements()) {
            for (Element enclosedElement : rootElement.getEnclosedElements()) {
                if (enclosedElement instanceof VariableElement) {
                    TypeMirror fieldTypeMirror = enclosedElement.asType();
                    TypeElement typeElement = (TypeElement) ((DeclaredType) fieldTypeMirror).asElement();
                    TypeParameterElement tpe = typeElement.getTypeParameters().get(0);
                    List<? extends AnnotationMirror> tpeAnn = tpe.getAnnotationMirrors();
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "On TPE: " + tpeAnn, enclosedElement);
                    List<? extends AnnotationMirror> boundAnn = tpe.getBounds().get(0).getAnnotationMirrors();
                    if (boundAnn.isEmpty()) {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, "No annotations on bound!", enclosedElement);
                    } else {
                        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "On bound: " + boundAnn, enclosedElement);
                    }
                }
            }
        }
        return true;
    }
}
