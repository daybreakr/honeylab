package com.honeycomb.annotation.compiler;

import com.google.auto.service.AutoService;
import com.honeycomb.annotation.StartupModule;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.HashSet;
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
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

@AutoService(Processor.class)
public class StartupModuleProcessor extends AbstractProcessor {
    private Filer mFiler;

    private static boolean sGenerated = false;

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(StartupModule.class.getCanonicalName());
        return types;
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnvironment.getFiler();
        sGenerated = false;
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<TypeElement> startupModules = new HashSet<>();

        for (Element element : roundEnvironment.getElementsAnnotatedWith(StartupModule.class)) {
            if (element.getKind() != ElementKind.CLASS) {
                throw new IllegalArgumentException("Only class can be annotated with @"
                        + StartupModule.class.getSimpleName());
            }

            startupModules.add((TypeElement) element);
        }

        if (!sGenerated || !startupModules.isEmpty()) {
            generateStartupModuleRegistry(startupModules);
        }

        return true;
    }

    private void generateStartupModuleRegistry(Set<TypeElement> startupModules) {
        MethodSpec.Builder start = MethodSpec.methodBuilder("start")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class);
        for (TypeElement startupModule : startupModules) {
            start.addStatement("$T.getInstance().start()", startupModule.asType());
        }

        TypeSpec startupModuleRegistry = TypeSpec.classBuilder("StartupModuleRegistry")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(start.build())
                .build();

        try {
            JavaFile.builder("com.honeycomb.lab", startupModuleRegistry).build().writeTo(mFiler);
            sGenerated = true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
