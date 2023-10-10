package com.example.core_autoloader_complier;

import com.example.core_autoloader_annotation.AutoLoader;

import java.util.Objects;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.util.Elements;

public class AutoLoaderAnnotationClass {
    // AutoLoader 中 value 值的包名
    private String autoLoaderValuePackageName;
    // AutoLoader 中 value 值的类名
    private String autoLoaderValueCanonicalName;
    // 全限定名
    private String qualifierName;

    public AutoLoaderAnnotationClass(TypeElement typeElement, Elements elementUtils) {
        AutoLoader autoLoader = typeElement.getAnnotation(AutoLoader.class);
        qualifierName = typeElement.getQualifiedName().toString();
        try {
            Class<?> value = autoLoader.value();
            autoLoaderValueCanonicalName = value.getCanonicalName();
            autoLoaderValuePackageName = value.getPackage().getName();
        } catch (MirroredTypeException mte) {
            DeclaredType classTypeMirror = (DeclaredType) mte.getTypeMirror();
            TypeElement classTypeElement = (TypeElement) classTypeMirror.asElement();
            autoLoaderValueCanonicalName = classTypeElement.getQualifiedName().toString();
            autoLoaderValuePackageName = elementUtils.getPackageOf(classTypeElement).asType().toString();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AutoLoaderAnnotationClass)) return false;
        AutoLoaderAnnotationClass that = (AutoLoaderAnnotationClass) obj;
        return Objects.equals(getAutoLoaderValuePackageName(), that.getAutoLoaderValuePackageName())
                && Objects.equals(getAutoLoaderValueCanonicalName(), that.getAutoLoaderValueCanonicalName())
                && Objects.equals(getQualifiedName(), that.getQualifiedName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAutoLoaderValuePackageName(), getAutoLoaderValueCanonicalName(), qualifierName);
    }

    public String getAutoLoaderValuePackageName() {
        return autoLoaderValuePackageName;
    }

    public String getAutoLoaderValueCanonicalName() {
        return autoLoaderValueCanonicalName;
    }

    public String getQualifiedName() {
        return qualifierName;
    }
}
