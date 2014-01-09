package com.github.mperry.fg

/**
 * Created by MarkPerry on 9/01/14.
 */
class TypeLambda {

    static GroovyClassLoader newLoader() {
        new GroovyClassLoader(this.class.classLoader)
    }

    static <A> Class<?> lambda(Class<?> c) {
       def clazz = newLoader().parseClass("class MyClass { }")
        clazz
    }


    static <A> Class<?> stateMonad(Class<?> c) {
        def clazz = newLoader().parseClass("class MyClass { }")
        clazz
    }


}
