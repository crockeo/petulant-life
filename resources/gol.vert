#version 150

uniform vec2 in_Size;
in vec3 in_vertexCoord;
out vec4 pass_Color;

void main() {
    // Scaling the input coordinate.
    mat3 scale = mat3(2 / in_Size.x, 0            , 0,
                      0            , 2 / in_Size.y, 0,
                      0            , 0            , 1
                     );

    // Translating the input coordinate.
    vec3 translate = vec3(-1, -1, 0);

    // The color to pass to the fragment shader.
    pass_Color = vec4(1, 1, 1, 1);

    // The output position.
    gl_Position = vec4(in_vertexCoord * scalify, 1);
}
