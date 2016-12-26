#version 120

attribute vec3 a_position;
attribute vec2 a_textureCoords;
attribute vec3 a_normal;

uniform mat4 u_camProjection;

varying vec2 v_diffuseUv_frag;

void main(){
    v_diffuseUv_frag = a_textureCoords;

    gl_Position = u_camProjection*vec4(a_position, 1.0);
}
