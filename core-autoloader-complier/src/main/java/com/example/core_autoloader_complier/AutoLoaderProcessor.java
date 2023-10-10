package com.example.core_autoloader_complier;

import com.example.core_autoloader_annotation.AutoLoader;
import com.google.auto.service.AutoService;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

@AutoService(Processor.class)
public class AutoLoaderProcessor extends AbstractProcessor {


    private Types typeUtils;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Logger.setMessager(processingEnv.getMessager());
        Logger.i("AutoLoaderProcessor init");
        typeUtils = processingEnv.getTypeUtils();
        filer = processingEnv.getFiler();
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Logger.i("CanonicalName is %s", AutoLoader.class.getCanonicalName());
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(AutoLoader.class.getCanonicalName());
        return annotations;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        try {
            Set<? extends Element> elementsAnnotatedWith = roundEnvironment.getElementsAnnotatedWith(AutoLoader.class);
            Logger.i("elementsAnnotatedWith size %s",elementsAnnotatedWith.size());
            for (Element annotatedElement : elementsAnnotatedWith) {
                // AutoLoader 注解的对象必须是 Class 类型
                if (annotatedElement.getKind() != ElementKind.CLASS) {
                    throw new ProcessingException(annotatedElement, "Only classes can be annotated with @%s", AutoLoader.class.getSimpleName());
                }
                TypeElement typeElement = (TypeElement) annotatedElement;
                AutoLoaderAnnotationClass autoLoaderAnnotationClass = new AutoLoaderAnnotationClass(typeElement,elementUtils);
                Logger.i("packageName:"+autoLoaderAnnotationClass.getAutoLoaderValuePackageName());
                Logger.i("className:"+autoLoaderAnnotationClass.getAutoLoaderValueCanonicalName());
                Logger.i("qualified:"+autoLoaderAnnotationClass.getQualifiedName());
            }
        } catch (ProcessingException e) {
            Logger.e(e.getElement(), e.getMessage());
        }
        return true;
    }
}
