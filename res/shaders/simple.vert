#version 140

in vec3 pos;
in vec3 normal;

uniform mat4 M;
uniform mat4 V;
uniform mat4 P;

out vec4 frag_position;
out vec3 frag_normal;

void main() {
    gl_Position = P*V*M*vec4(pos, 1);
    frag_position = M*vec4(pos, 1);
    frag_normal = normalize((M*vec4(normal, 0)).xyz);
}