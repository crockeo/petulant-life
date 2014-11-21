#version 330

uniform vec2 in_Size;
layout(location = 0)in vec2 vert;

void main() {
    vec2 tempVert = vert;

    // Scaling the x-axis.
    tempVert.x /= (in_Size.x / 2);
    tempVert.x -= 1;

    // Scaling the y-axis.
    tempVert.y /= (in_Size.y / 2);
    tempVert.y -= 1;

    // Setting the OpenGL position.
    gl_Position = vec4(tempVert, 0, 1);
}
