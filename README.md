# petulant-life

A Clojure application to do cool visual game-of-life stuff that's also reactive
to music and sounds.

## General Gameplan

We'll quantize the sound wavelengths that we're getting into about four or
five steps. Each of these quantizations will influence a separate
layer of game of life and correspond to a different color. Then the
visualization will be created by merging the different layers of the
game of life in some manner to create more color variations.

## Todos:

### Visuals

* *petulant-life.rendering* - Implement `loadShaderProgram`
* *petulant-life.rendering* - Implement `drawVAO`

### Game of Life

* *petulant-life.life* - Implement multi-layered Game-of-Life stuff

## Usage

I don't know.

## License

Copyright © 2014 Cerek Hillen, Geoff Shannon

Distributed under the MIT License.
