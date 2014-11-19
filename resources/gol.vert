#version 150

in vec2 in_vertexCoord;
out vec4 pass_Color;

void main() {
    gl_Position = vec4(in_vertexCoord, 0, 1);
    pass_Color = vec4(1, 1, 1, 1);
}
