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


    static <S> Class<State<S, ?>> stateMonad(Class<S> c) {
        def clazz = newLoader().parseClass("""
            class State${c.simpleName}MonadMP<A> extends Monad<State<${c.name}, A>> {

            }
            """)
        clazz
    }

    static <A> State stateInstance(Class<?> c) {
        c.newInstance()

    }



}
