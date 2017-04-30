package com.artemkopan.movie.injection.builder


interface SubComponentBuilder<out V> {
    fun build(): V
}
