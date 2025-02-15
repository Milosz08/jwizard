package pl.jwizard.jwc.core.jda.event

import pl.jwizard.jwl.ioc.stereotype.SingletonComponent

// put on IoC components extending ListenerAdapter class
@SingletonComponent
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class JdaEventListenerBean
