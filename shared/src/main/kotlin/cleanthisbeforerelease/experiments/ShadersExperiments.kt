package org.splitties.compose.oclock.sample.cleanthisbeforerelease.experiments

import android.graphics.RuntimeShader
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ShaderBrush
import androidx.wear.compose.ui.tooling.preview.WearPreviewLargeRound
import org.intellij.lang.annotations.Language
import org.splitties.compose.oclock.LocalIsAmbient
import org.splitties.compose.oclock.LocalTime
import org.splitties.compose.oclock.OClockCanvas
import org.splitties.compose.oclock.OClockRootCanvas
import org.splitties.compose.oclock.sample.extensions.rememberStateWithSize

@RequiresApi(33)
@Composable
fun ShaderExperiments(
    shader: RuntimeShader = remember { createSkiaExampleShader1() },
    hideInAmbient: Boolean = true
) {
    val brush = remember(shader) { ShaderBrush(shader) }
    val shaderUpdater = rememberStateWithSize(shader) {
        shader.setFloatUniform("iResolution", size.width, size.height)
    }
    val time = LocalTime.current
    val isAmbient by LocalIsAmbient.current
    if (isAmbient && hideInAmbient) return
    val t by produceDrawLoopCounter(1f)
    OClockCanvas {
        shaderUpdater.get()
        shader.setFloatUniform("iTime", t)
        drawRect(brush)
    }
}


@RequiresApi(33)
fun createWarpSpeedShader() = RuntimeShader(
    """
        // 'Warp Speed 2'
        // David Hoskins 2015.
        // License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.

        // Fork of:-   https://www.shadertoy.com/view/Msl3WH
        //----------------------------------------------------------------------------------------
        uniform float2 iResolution;      // Viewport resolution (pixels)
        uniform float  iTime;            // Shader playback time (s)

        vec4 main( in float2 fragCoord )
        {
            float s = 0.0, v = 0.0;
            vec2 uv = (fragCoord / iResolution.xy) * 2.0 - 1.;
            float iTime = (iTime-2.0)*58.0;
            vec3 col = vec3(0);
            vec3 init = vec3(sin(iTime * .0032)*.3, .35 - cos(iTime * .005)*.3, iTime * 0.002);
            for (int r = 0; r < 100; r++) 
            {
                vec3 p = init + s * vec3(uv, 0.05);
                p.z = fract(p.z);
                // Thanks to Kali's little chaotic loop...
                for (int i=0; i < 10; i++)	p = abs(p * 2.04) / dot(p, p) - .9;
                v += pow(dot(p, p), .7) * .06;
                col +=  vec3(v * 0.2+.4, 12.-s*2., .1 + v * 1.) * v * 0.00003;
                s += .025;
            }
            return vec4(clamp(col, 0.0, 1.0), 1.0);
        }
    """.trimIndent()
)

@RequiresApi(33)
fun createSkiaExampleShader1() = RuntimeShader(
    """
        //----------------------------------------------------------------------------------------
        uniform float2 iResolution;      // Viewport resolution (pixels)
        uniform float  iTime;            // Shader playback time (s)

        // Source: @notargs https://twitter.com/notargs/status/1250468645030858753
        float f(vec3 p) {
            p.z -= iTime * 10.;
            float a = p.z * .1;
            p.xy *= mat2(cos(a), sin(a), -sin(a), cos(a));
            return .1 - length(cos(p.xy) + sin(p.yz));
        }

        half4 main(vec2 fragcoord) { 
            vec3 d = .5 - fragcoord.xy1 / iResolution.y;
            vec3 p=vec3(0);
            for (int i = 0; i < 32; i++) {
              p += f(p) * d;
            }
            return ((sin(p) + vec3(2, 5, 0)) / length(p)).xyz1;
        }

    """.trimIndent()
)

@RequiresApi(33)
fun createSeascapeShader() = RuntimeShader(
    """
        //----------------------------------------------------------------------------------------
        uniform float2 iResolution;      // Viewport resolution (pixels)
        uniform float  iTime;            // Shader playback time (s)

        /*
         * "Seascape" by Alexander Alekseev aka TDM - 2014
         * License Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
         * Contact: tdmaav@gmail.com
         */

        const int NUM_STEPS = 8;
        const float2 iMouse = float2(150.0, 150.0);
        const float PI	 	= 3.141592;
        const float EPSILON	= 1e-3;
//        const float EPSILON_NRM = (0.1 / iResolution.x);
        //#define AA

        // sea
        const int ITER_GEOMETRY = 3;
        const int ITER_FRAGMENT = 5;
        const float SEA_HEIGHT = 0.6;
        const float SEA_CHOPPY = 4.0;
        const float SEA_SPEED = 0.8;
        const float SEA_FREQ = 0.16;
        const vec3 SEA_BASE = vec3(0.0,0.09,0.18);
        const vec3 SEA_WATER_COLOR = vec3(0.8,0.9,0.6)*0.6;
//        const float SEA_TIME = (1.0 + iTime * SEA_SPEED);
        const mat2 octave_m = mat2(1.6,1.2,-1.2,1.6);
        
        float SEA_TIME() {
            return (1.0 + iTime * SEA_SPEED);        
        }

        // math
        mat3 fromEuler(vec3 ang) {
        	vec2 a1 = vec2(sin(ang.x),cos(ang.x));
            vec2 a2 = vec2(sin(ang.y),cos(ang.y));
            vec2 a3 = vec2(sin(ang.z),cos(ang.z));
            mat3 m;
            m[0] = vec3(a1.y*a3.y+a1.x*a2.x*a3.x,a1.y*a2.x*a3.x+a3.y*a1.x,-a2.y*a3.x);
        	m[1] = vec3(-a2.y*a1.x,a1.y*a2.y,a2.x);
        	m[2] = vec3(a3.y*a1.x*a2.x+a1.y*a3.x,a1.x*a3.x-a1.y*a3.y*a2.x,a2.y*a3.y);
        	return m;
        }
        float hash( vec2 p ) {
        	float h = dot(p,vec2(127.1,311.7));	
            return fract(sin(h)*43758.5453123);
        }
        float noise( in vec2 p ) {
            vec2 i = floor( p );
            vec2 f = fract( p );	
        	vec2 u = f*f*(3.0-2.0*f);
            return -1.0+2.0*mix( mix( hash( i + vec2(0.0,0.0) ), 
                             hash( i + vec2(1.0,0.0) ), u.x),
                        mix( hash( i + vec2(0.0,1.0) ), 
                             hash( i + vec2(1.0,1.0) ), u.x), u.y);
        }

        // lighting
        float diffuse(vec3 n,vec3 l,float p) {
            return pow(dot(n,l) * 0.4 + 0.6,p);
        }
        float specular(vec3 n,vec3 l,vec3 e,float s) {    
            float nrm = (s + 8.0) / (PI * 8.0);
            return pow(max(dot(reflect(e,n),l),0.0),s) * nrm;
        }

        // sky
        vec3 getSkyColor(vec3 e) {
            e.y = (max(e.y,0.0)*0.8+0.2)*0.8;
            return vec3(pow(1.0-e.y,2.0), 1.0-e.y, 0.6+(1.0-e.y)*0.4) * 1.1;
        }

        // sea
        float sea_octave(vec2 uv, float choppy) {
            uv += noise(uv);        
            vec2 wv = 1.0-abs(sin(uv));
            vec2 swv = abs(cos(uv));    
            wv = mix(wv,swv,wv);
            return pow(1.0-pow(wv.x * wv.y,0.65),choppy);
        }

        float map(vec3 p) {
            float freq = SEA_FREQ;
            float amp = SEA_HEIGHT;
            float choppy = SEA_CHOPPY;
            vec2 uv = p.xz; uv.x *= 0.75;
            
            float d, h = 0.0;    
            for(int i = 0; i < ITER_GEOMETRY; i++) {        
            	d = sea_octave((uv+SEA_TIME())*freq,choppy);
            	d += sea_octave((uv-SEA_TIME())*freq,choppy);
                h += d * amp;        
            	uv *= octave_m; freq *= 1.9; amp *= 0.22;
                choppy = mix(choppy,1.0,0.2);
            }
            return p.y - h;
        }

        float map_detailed(vec3 p) {
            float freq = SEA_FREQ;
            float amp = SEA_HEIGHT;
            float choppy = SEA_CHOPPY;
            vec2 uv = p.xz; uv.x *= 0.75;
            
            float d, h = 0.0;    
            for(int i = 0; i < ITER_FRAGMENT; i++) {        
            	d = sea_octave((uv+SEA_TIME())*freq,choppy);
            	d += sea_octave((uv-SEA_TIME())*freq,choppy);
                h += d * amp;        
            	uv *= octave_m; freq *= 1.9; amp *= 0.22;
                choppy = mix(choppy,1.0,0.2);
            }
            return p.y - h;
        }

        vec3 getSeaColor(vec3 p, vec3 n, vec3 l, vec3 eye, vec3 dist) {  
            float fresnel = clamp(1.0 - dot(n,-eye), 0.0, 1.0);
            fresnel = min(pow(fresnel,3.0), 0.5);
                
            vec3 reflected = getSkyColor(reflect(eye,n));    
            vec3 refracted = SEA_BASE + diffuse(n,l,80.0) * SEA_WATER_COLOR * 0.12; 
            
            vec3 color = mix(refracted,reflected,fresnel);
            
            float atten = max(1.0 - dot(dist,dist) * 0.001, 0.0);
            color += SEA_WATER_COLOR * (p.y - SEA_HEIGHT) * 0.18 * atten;
            
            color += vec3(specular(n,l,eye,60.0));
            
            return color;
        }

        // tracing
        vec3 getNormal(vec3 p, float eps) {
            vec3 n;
            n.y = map_detailed(p);    
            n.x = map_detailed(vec3(p.x+eps,p.y,p.z)) - n.y;
            n.z = map_detailed(vec3(p.x,p.y,p.z+eps)) - n.y;
            n.y = eps;
            return normalize(n);
        }

        float heightMapTracing(vec3 ori, vec3 dir, out vec3 p) {  
            float tm = 0.0;
            float tx = 1000.0;    
            float hx = map(ori + dir * tx);
            if(hx > 0.0) {
                p = ori + dir * tx;
                return tx;   
            }
            float hm = map(ori + dir * tm);    
            float tmid = 0.0;
            for(int i = 0; i < NUM_STEPS; i++) {
                tmid = mix(tm,tx, hm/(hm-hx));                   
                p = ori + dir * tmid;                   
            	float hmid = map(p);
        		if(hmid < 0.0) {
                	tx = tmid;
                    hx = hmid;
                } else {
                    tm = tmid;
                    hm = hmid;
                }
            }
            return tmid;
        }

        vec3 getPixel(in vec2 coord, float iTime) {    
            vec2 uv = coord / iResolution.xy;
            uv = uv * 2.0 - 1.0;
            uv.x *= iResolution.x / iResolution.y;    
                
            // ray
            vec3 ang = vec3(sin(iTime*3.0)*4.1,sin(iTime)*0.2+0.3,iTime);    
            vec3 ori = vec3(0.0,3.5,iTime*5.0);
            vec3 dir = normalize(vec3(uv.xy,-2.0)); dir.z += length(uv) * 0.14;
            dir = normalize(dir) * fromEuler(ang);
            
            // tracing
            vec3 p;
            heightMapTracing(ori,dir,p);
            vec3 dist = p - ori;
            vec3 n = getNormal(p, dot(dist,dist) * 0.1 / iResolution.x);
            vec3 light = normalize(vec3(0.0,1.0,0.8)); 
                     
            // color
            return mix(
                getSkyColor(dir),
                getSeaColor(p,n,light,dir,dist),
            	pow(smoothstep(0.0,-0.02,dir.y),0.2));
        }

        // main
        vec4 main(in vec2 fragCoord ) {
            float iTime = 15 * 0.3;// + iMouse.x*0.01;
//            float iTime = iTime * 0.3;// + iMouse.x*0.01;
        	
//        #ifdef AA
//            vec3 color = vec3(0.0);
//            for(int i = -1; i <= 1; i++) {
//                for(int j = -1; j <= 1; j++) {
//                	vec2 uv = fragCoord+vec2(i,j)/3.0;
//            		color += getPixel(uv, iTime);
//                }
//            }
//            color /= 9.0;
//        #else
            vec3 color = getPixel(fragCoord, iTime);
//        #endif
            
            // post
        	return vec4(pow(color,vec3(0.65)), 1.0);
        }
    """.trimIndent()
)

@RequiresApi(33)
@Language("AGSL")
fun createShaderArtCodingIntroShader() = """
uniform float2 iResolution;//px
uniform float iTime;//s

/* This animation is the material of my first youtube tutorial about creative 
   coding, which is a video in which I try to introduce programmers to GLSL 
   and to the wonderful world of shaders, while also trying to share my recent 
   passion for this community.
                                       Video URL: https://youtu.be/f4s1h2YETNY
*/

//https://iquilezles.org/articles/palettes/
vec3 palette( float t ) {
    vec3 a = vec3(0.5, 0.5, 0.5);
    vec3 b = vec3(0.5, 0.5, 0.5);
    vec3 c = vec3(1.0, 1.0, 1.0);
    vec3 d = vec3(0.263,0.416,0.557);

    return a + b*cos( 6.28318*(c*t+d) );
}


vec4 main(vec2 fragCoords) {
    vec2 uv = (fragCoords * 2.0 - iResolution.xy) / iResolution.y;
    vec2 uv0 = uv;
    vec3 finalColor = vec3(0.0);
    
    for (float i = 0.0; i < 4.0; i++) {
        uv = fract(uv * 1.5) - 0.5;

        float d = length(uv) * exp(-length(uv0));

        vec3 col = palette(length(uv0) + i*.4 + iTime*.4);

        d = sin(d*8. + iTime)/8.;
        d = abs(d);

        d = pow(0.01 / d, 1.2);

        finalColor += col * d;
    }
    return vec4(finalColor *.3, 1);
}
""".trimIndent().let { RuntimeShader(it) }

@RequiresApi(33)
@Language("AGSL")
fun createMatrixRainShader() = """
// https://www.shadertoy.com/view/XsXBWX
uniform float2 iResolution;//px
uniform float iTime;//s

vec4 main(vec2 fragCoords) {
    vec2 u = fragCoords / 10.;
    vec4 iDate = vec4(2024., 1., 6., iTime);
    vec4 o = -iDate;
    o.g=.5-6.*fract((u.x*.2+u.y*.01)*fract(u.x*.61)-o.w);
    o=vec4(u.x+9.,u.x,u.x-9.,fract(u.y*-.61));
    o=o.w-o.w*abs(20.*fract((u.y*.2+(o.g-o*o.w*o.w)*.01)*(1.-o.w)+iDate.w)-1.);
    return o;
}
""".trimIndent().let { RuntimeShader(it) }

@RequiresApi(33)
@Language("AGSL")
fun createTestShader() = """
uniform float2 iResolution;//px
uniform float iTime;//s

vec4 main(vec2 fragCoords) {
    vec2 uv = fragCoords / iResolution;
    return vec4((sin(iTime * 2.) + 1)* .31,uv.xy,1.0);
}
""".trimIndent().let { RuntimeShader(it) }

@Composable
private fun produceDrawLoopCounter(speed: Float = 1f): State<Float> {
    return produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f * speed
            }
        }
    }
}

@RequiresApi(33)
@WearPreviewLargeRound
@Composable
private fun ShaderExperimentsPreview() = OClockRootCanvas {
    ShaderExperiments()
}

@RequiresApi(33)
@WearPreviewLargeRound
@Composable
private fun ShaderExperiments2Preview() = OClockRootCanvas {
    ShaderExperiments(remember { createTestShader() })
}

@RequiresApi(33)
@WearPreviewLargeRound
@Composable
private fun ShaderArtCodingIntroPreview() = OClockRootCanvas {
    ShaderExperiments(remember { createShaderArtCodingIntroShader() })
}

@RequiresApi(33)
@WearPreviewLargeRound
@Composable
private fun ShaderMatrixRainPreview() = OClockRootCanvas {
    ShaderExperiments(remember { createMatrixRainShader() })
}
