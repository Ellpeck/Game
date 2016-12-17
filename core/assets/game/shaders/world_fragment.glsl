uniform sampler2D u_diffuseTexture;

varying vec2 v_diffuseUv_frag;

void main(){
    gl_FragColor = texture2D(u_diffuseTexture, v_diffuseUv_frag);
}
