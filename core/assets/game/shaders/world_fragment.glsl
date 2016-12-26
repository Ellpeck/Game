#version 120

uniform sampler2D u_diffuseTexture;

varying vec2 v_diffuseUv_frag;

void main(){
    vec4 color = texture2D(u_diffuseTexture, v_diffuseUv_frag);
    if(color.a == 0F){
        discard;
    }
    else if(color.a < 1F){
        color.a = 1F;
    }

    gl_FragColor = color;
}
