#version 330

layout(location = 0)in vec2 vert;

void main() {
    vec2 in_Size = vec2(640, 480);
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
