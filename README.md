# `TYPE_USE` annotations not visible for processor

## Project structure

3 Modules.

- `base` module contains:
  - `LookAt` triggers the annotation processor.
  - `TypeUseAnnotation` is a generic `TYPE_USE` annotation with `@Retention(RUNTIME)`.
  - `AnnotatedClassBase` is one of the example classes with one type parameter, declared as: 
```java
class AnnotatedClassBase<@TypeUseAnnotation T extends @TypeUseAnnotation String>
```
- `target` module depends on `base` and contains:
  - `AnnotatedClassTarget` class, identical to `AnnotatedClassBase`
  - `UseSite`, which has one field of `AnnotatedClassBase` and `AnnotatedClassBase` each, annotated with `@LookAt` to trigger the processor to look at the fields.
- `processor` is the annotation processor, it will process the `UseSite` class in the `target` module.

## Build steps

Run `./gradlew compileJava`. The build will fail because the annotation on the type parameter bound of `AnnotatedClassBase` is missing.

## Issue description

Expectation: `AnnotatedClassBase` and `AnnotatedClassTarget` should have identical annotations on the type parameter and its bound, when inspected with the processor.

Actual behavior: Both classes have the annotation on the type variable, but the annotation on its bound does not show up on `AnnotatedClassBase`.

From reading the code, it seems like `TYPE_USE` annotations are simply not copied to the `TypeMirror`s of the bounds. The `TypeParameterElement` [has special handling](https://code.yawk.at/java/17/jdk.compiler/com/sun/tools/javac/code/Symbol.java#916) that looks at the annotations in the type attributes of the class where the parameter is declared, but there is no such handling for `TypeMirror`.