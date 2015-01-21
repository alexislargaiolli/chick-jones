package fr.alex.games;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class LightManager {
	ShaderProgram program;
	final Vector3 ambientColor = new Vector3();
	final Vector3 lightColor = new Vector3();
	final Vector3 lightPosition = new Vector3();
	public Vector3 getLightPosition() {
		return lightPosition;
	}

	final Vector2 resolution = new Vector2();
	final Vector3 attenuation = new Vector3();
	
	public LightManager(SpriteBatch batch){
		program = createShader();
		batch.setShader(program);
		ambientColor.set(1f, 1f, 1f);
		lightColor.set(.5f, .5f, .5f);
		lightPosition.set(4, 4, .5f);
		attenuation.set(.5f, .5f, .5f);
	}
	
	public void resize(float width, float height){
		resolution.set(width, height);
	}
	
	public void begin(){
		program.setUniformi("yInvert", 0);
		program.setUniformf("resolution", resolution);
		program.setUniformf("ambientColor", ambientColor);
		program.setUniformf("ambientIntensity", .5f);
		program.setUniformf("attenuation", attenuation);
		program.setUniformf("light", lightPosition);
		program.setUniformf("lightColor", lightColor);
		program.setUniformi("useNormals", 1);
		program.setUniformi("useShadow", 1);
		program.setUniformf("strength", 1);
	}
	
	private ShaderProgram createShader () {
		String vert = "attribute vec4 a_position;\n" //
			+ "attribute vec4 a_color;\n" //
			+ "attribute vec2 a_texCoord0;\n" //
			+ "uniform mat4 u_proj;\n" //
			+ "uniform mat4 u_trans;\n" //
			+ "uniform mat4 u_projTrans;\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "\n" //
			+ "void main()\n" //
			+ "{\n" //
			+ "   v_color = a_color;\n" //
			+ "   v_texCoords = a_texCoord0;\n" //
			+ "   gl_Position =  u_projTrans * a_position;\n" //
			+ "}\n" //
			+ "";

		String frag = "#ifdef GL_ES\n" //
			+ "precision mediump float;\n" //
			+ "#endif\n" //
			+ "varying vec4 v_color;\n" //
			+ "varying vec2 v_texCoords;\n" //
			+ "uniform sampler2D u_texture;\n" //
			+ "uniform sampler2D u_normals;\n" //
			+ "uniform vec3 light;\n" //
			+ "uniform vec3 ambientColor;\n" //
			+ "uniform float ambientIntensity; \n" //
			+ "uniform vec2 resolution;\n" //
			+ "uniform vec3 lightColor;\n" //
			+ "uniform bool useNormals;\n" //
			+ "uniform bool useShadow;\n" //
			+ "uniform vec3 attenuation;\n" //
			+ "uniform float strength;\n" //
			+ "uniform bool yInvert;\n" //
			+ "\n" //
			+ "void main() {\n" //
			+ "  // sample color & normals from our textures\n" //
			+ "  vec4 color = texture2D(u_texture, v_texCoords.st);\n" //
			+ "  vec3 nColor = texture2D(u_normals, v_texCoords.st).rgb;\n" //
			+ "\n" //
			+ "  // some bump map programs will need the Y value flipped..\n" //
			+ "  nColor.g = yInvert ? 1.0 - nColor.g : nColor.g;\n" //
			+ "\n" //
			+ "  // this is for debugging purposes, allowing us to lower the intensity of our bump map\n" //
			+ "  vec3 nBase = vec3(0.5, 0.5, 1.0);\n" //
			+ "  nColor = mix(nBase, nColor, strength);\n" //
			+ "\n" //
			+ "  // normals need to be converted to [-1.0, 1.0] range and normalized\n" //
			+ "  vec3 normal = normalize(nColor * 2.0 - 1.0);\n" //
			+ "\n" //
			+ "  // here we do a simple distance calculation\n" //
			+ "  vec3 deltaPos = vec3( (light.xy - gl_FragCoord.xy) / resolution.xy, light.z );\n" //
			+ "\n" //
			+ "  vec3 lightDir = normalize(deltaPos);\n" //
			+ "  float lambert = useNormals ? clamp(dot(normal, lightDir), 0.0, 1.0) : 1.0;\n" //
			+ "  \n" //
			+ "  // now let's get a nice little falloff\n" //
			+ "  float d = sqrt(dot(deltaPos, deltaPos));  \n" //
			+ "  float att = useShadow ? 1.0 / ( attenuation.x + (attenuation.y*d) + (attenuation.z*d*d) ) : 1.0;\n" //
			+ "  \n" //
			+ "  vec3 result = (ambientColor * ambientIntensity) + (lightColor.rgb * lambert) * att;\n" //
			+ "  result *= color.rgb;\n" //
			+ "  \n" //
			+ "  gl_FragColor = v_color * vec4(result, color.a);\n" //
			+ "}";

		// System.out.println("VERTEX PROGRAM:\n------------\n\n" + vert);
		// System.out.println("FRAGMENT PROGRAM:\n------------\n\n" + frag);
		ShaderProgram program = new ShaderProgram(vert, frag);
		ShaderProgram.pedantic = false;
		if (!program.isCompiled()) throw new IllegalArgumentException("Error compiling shader: " + program.getLog());

		program.begin();
		program.setUniformi("u_texture", 0);
		program.setUniformi("u_normals", 1);
		program.end();

		return program;
	}
}
