package net.simplifiedcoding.fish.Character_Zero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.annotation.Nullable;
import android.util.Log;

import net.simplifiedcoding.fish.R;


public final class Character_Zero_Constants {

    public int animation_frame = 0;
    private double adapter;


    Character_Zero_Constants(Context context) {
        // restrict instantiation
        zero_sprite_sheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.zero_sprite_sheet);

        // matriz para girar horizontalmente a la imagen
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);

        // obetenemos la densidad de pixeles en pantalla para hacer cuadrar la imagen dentro del sprite sheet
        adapter = context.getResources().getDisplayMetrics().density;

        //rellenamos cada bitmap[] con sus imagenes

        //animaciones hacia la izq
        iddle_animation_izq = generateAnimation(iddle,matrix);
        iddle_low_hp_animation_izq = generateAnimation(iddle_low_hp,matrix);
        walk_start_animation_izq = generateAnimation(walk_start,matrix);
        walk_loop_animation_izq = generateAnimation(walk_loop,matrix);
        walk_loop_transition_to_idle_animation_izq = generateAnimation(walk_loop_transition_to_idle,matrix);
        slash_A_animation_izq = generateAnimation(slash_A,matrix);
        slash_B_animation_izq = generateAnimation(slash_B,matrix);
        slash_C_animation_izq = generateAnimation(slash_C,matrix);
        slash_End_animation_izq = generateAnimation(slash_End,matrix);
        dash_start_animation_izq = generateAnimation(dash_start,matrix);
        dash_loop_animation_izq = generateAnimation(dash_loop,matrix);
        dash_end_animation_izq = generateAnimation(dash_end,matrix);
        jump_animation_izq = generateAnimation(jump,matrix);
        jump_start_izq = generateAnimation(jump_start,matrix);
        jump_elevating_loop_izq = generateAnimation(jump_elevating_loop,matrix);
        jump_start_descending_izq = generateAnimation(jump_start_descending,matrix);
        jump_descending_loop_izq = generateAnimation(jump_descending_loop,matrix);
        jump_land_animation_izq = generateAnimation(jump_land,matrix);
        zero_buster_animation_izq = generateAnimation(zero_buster,matrix);
        zero_air_attack_animation_izq = generateAnimation(zero_air_attack,matrix);

        //animaciones hacia la der
        iddle_animation_der = generateAnimation(iddle,null);
        iddle_low_hp_animation_der = generateAnimation(iddle_low_hp,null);
        walk_start_animation_der = generateAnimation(walk_start,null);
        walk_loop_animation_der = generateAnimation(walk_loop,null);
        walk_loop_transition_to_idle_animation_der = generateAnimation(walk_loop_transition_to_idle,null);
        slash_A_animation_der = generateAnimation(slash_A,null);
        slash_B_animation_der = generateAnimation(slash_B,null);
        slash_C_animation_der = generateAnimation(slash_C,null);
        slash_End_animation_der = generateAnimation(slash_End,null);
        dash_start_animation_der = generateAnimation(dash_start,null);
        dash_loop_animation_der = generateAnimation(dash_loop,null);
        dash_end_animation_der = generateAnimation(dash_end,null);
        jump_animation_der = generateAnimation(jump,null);
        jump_start_der = generateAnimation(jump_start,null);
        jump_elevating_loop_der = generateAnimation(jump_elevating_loop,null);
        jump_start_descending_der = generateAnimation(jump_start_descending,null);
        jump_descending_loop_der = generateAnimation(jump_descending_loop,null);
        jump_land_animation_der = generateAnimation(jump_land,null);
        zero_buster_animation_der = generateAnimation(zero_buster,null);
        zero_air_attack_animation_der = generateAnimation(zero_air_attack,null);

        //Log.i("msj Zero_Constants","CARGADAS LAS CONSTANTES");
        //Log.i("msj Zero_Constants","iddle_animation_der len = "+ this.iddle_animation_der.length);

    }

    //main sprite sheet
    Bitmap zero_sprite_sheet;


    //posibles estados fisicos del personaje
    public static int state; //borrar luego
    public static final int quieto_en_el_suelo = 0;
    public static final int en_el_aire = 1;
    public static final int moviendote_a_la_izquierda = 2;
    public static final int moviendote_a_la_derecha = 3;
    public static final int low_hp = 4;
    public static final int durante_un_dash = 5;
    public static final int durante_ataque_1 = 6;
    public static final int durante_ataque_2 = 7;
    public static final int durante_ataque_3 = 8;
    public static final int disparando = 9;
    public static final int getting_hurt = 10;


    //coordinates
    public static final double[][] iddle = {{10,102,43,47},
            {67,102,43,47},
            {125,101,42,48},
            {182,102,42,47},
            {238,102,43,47},
            {295,102,43,47}};
    public static final double[][] iddle_low_hp = {{353,102,42,47},
            {412,101,40,48},
            {467,102,42,47},
            {524,103,42,46},
            {581,103,42,46},
            {638,103,42,46}};
    public static final double[][] walk_start = {{13,179,36,45},
            {59,178,38,46}};
    public static final double[][] walk_loop = {{100,178,51,44},
            {167,179,50,45},
            {218,178,48,46},
            {272,177,46,47},
            {328,176,44,48},
            {380,176,40,48},
            {429,177,45,47},
            {488,178,49,45},
            {541,179,45,46},
            {602,178,50,47},
            {658,177,46,48},
            {713,176,43,49},
            {764,176,42,49},
            {813,177,45,48}};
    public static final double[][] walk_loop_transition_to_idle = {{867,178,43,47},
            {924,178,43,47}};
    public static final double[][] slash_A = {{6,252,39,46},
            {55,251,39,48},
            {101,236,49,63},
            {173,236,78,63},
            {268,239,87,60},
            {369,251,91,48},
            {465,250,83,49},
            {555,254,70,45},
            {636,254,66,45},
            {718,254,57,45},
            {783,254,48,45},
            {848,254,45,45},
            {907,253,49,45}};
    public static final double[][] slash_B = {{6,389,52,44},
            {62,388,66,45},
            {145,388,83,45},
            {233,387,103,45},
            {347,387,66,46},
            {427,387,67,46},
            {509,387,73,46},
            {594,387,63,46},
            {674,387,55,46},
            {749,387,53,46},
            {813,387,46,46},
            {865,387,43,46}};
    public static final double[][] slash_C = {{8,516,46,50},
            {72,517,47,50},
            {126,508,54,59},
            {193,503,96,64},
            {310,503,103,71},
            {431,509,104,66},
            {552,505,104,70},
            {673,508,72,67},
            {762,520,58,55},
            {835,528,53,43},
            {900,528,51,39},
            {965,528,51,39},
            {1030,528,51,39},
            {1095,528,51,39},
            {1155,523,41,44}};
    public static final double[][] slash_End = {{963,253,47,46},
            {1023,253,44,46},
            {1083,253,41,46},
            {1138,253,43,46}};
    public static final double[][] dash_start = {{3,715,47,45},
            {53,725,56,42}};
    public static final double[][] dash_loop = {{116,726,58,35},
            {181,726,58,35},
            {246,726,58,35}};
    public static final double[][] dash_end = {{312,715,46,44},
            {360,713,42,45},
            {412,712,42,46},
            {461,712,42,46}};
    public static final double[][] jump = {{518,711,39,48},
            {568,708,43,56},
            {623,708,44,56},
            {685,708,43,57},
            {743,708,43,56},
            {792,709,39,52},
            {846,708,40,55},
            {898,703,36,64},
            {946,692,35,77},
            {995,690,35,79},
            {1042,689,37,80},
            {1089,692,39,77}};
    public static final double[][] jump_start = {{518,711,39,48},
            {568,708,43,56},
            {623,708,44,56}};
    public static final double[][] jump_elevating_loop = {{568,708,43,56},
            {623,708,44,56}};
    public static final double[][] jump_start_descending = {{685,708,43,57},
            {743,708,43,56},
            {792,709,39,52},
            {846,708,40,55},
            {898,703,36,64},
            {946,692,35,77},
            {995,690,35,79},
            {1042,689,37,80},
            {1089,692,39,77}};
    public static final double[][] jump_descending_loop = {{946,692,35,77},
            {995,690,35,79},
            {1042,689,37,80},
            {1089,692,39,77}};
    public static final double[][] jump_land = {{1137,706,40,59},
            {1192,716,42,43}};
    public static final double[][] zero_buster = {{11,1344,45,49},
            {82,1344,45,49},
            {154,1344,45,49},
            {225,1344,45,49},
            {296,1344,45,49},
            {367,1344,45,49},
            {423,1344,45,49},
            {482,1343,41,50},
            {538,1342,34,51},
            {586,1344,40,49},
            {635,1344,45,49}};
    public static final double[][] zero_air_attack = {{14,891,49,57},
            {79,886,52,62},
            {142,885,70,60},
            {230,877,89,65},
            {336,871,100,74},
            {440,869,54,76},
            {499,870,52,75},
            {556,868,41,78},
            {612,866,36,79}};

    //animation
    //izq
    public final Bitmap[] iddle_animation_izq;
    public final Bitmap[] iddle_low_hp_animation_izq;
    public final Bitmap[] walk_start_animation_izq;
    public final Bitmap[] walk_loop_animation_izq;
    public final Bitmap[] walk_loop_transition_to_idle_animation_izq;
    public final Bitmap[] slash_A_animation_izq;
    public final Bitmap[] slash_B_animation_izq;
    public final Bitmap[] slash_C_animation_izq;
    public final Bitmap[] slash_End_animation_izq;
    public final Bitmap[] dash_start_animation_izq;
    public final Bitmap[] dash_loop_animation_izq;
    public final Bitmap[] dash_end_animation_izq;
    public final Bitmap[] jump_animation_izq;
    public final Bitmap[] jump_start_izq;
    public final Bitmap[] jump_elevating_loop_izq;
    public final Bitmap[] jump_start_descending_izq;
    public final Bitmap[] jump_descending_loop_izq;
    public final Bitmap[] jump_land_animation_izq;
    public final Bitmap[] zero_buster_animation_izq;
    public final Bitmap[] zero_air_attack_animation_izq;

    //der
    public final Bitmap[] iddle_animation_der;
    public final Bitmap[] iddle_low_hp_animation_der;
    public final Bitmap[] walk_start_animation_der;
    public final Bitmap[] walk_loop_animation_der;
    public final Bitmap[] walk_loop_transition_to_idle_animation_der;
    public final Bitmap[] slash_A_animation_der;
    public final Bitmap[] slash_B_animation_der;
    public final Bitmap[] slash_C_animation_der;
    public final Bitmap[] slash_End_animation_der;
    public final Bitmap[] dash_start_animation_der;
    public final Bitmap[] dash_loop_animation_der;
    public final Bitmap[] dash_end_animation_der;
    public final Bitmap[] jump_animation_der;
    public final Bitmap[] jump_start_der;
    public final Bitmap[] jump_elevating_loop_der;
    public final Bitmap[] jump_start_descending_der;
    public final Bitmap[] jump_descending_loop_der;
    public final Bitmap[] jump_land_animation_der;
    public final Bitmap[] zero_buster_animation_der;
    public final Bitmap[] zero_air_attack_animation_der;

    protected Bitmap createSubImageAt(double col, double row, double col_len, double row_len, Matrix matrix, boolean type)  {
        // createBitmap(bitmap, x, y, width, height).
        int sprite_width=0;
        int sprite_height=0;
        Bitmap subImage;
        if (type) {//para sprite-sheets regulares
            subImage = Bitmap.createBitmap(zero_sprite_sheet, (int)(col * sprite_width), (int)(row * sprite_height), sprite_width, sprite_height);
        }else{
            subImage = Bitmap.createBitmap(zero_sprite_sheet, (int)col, (int)row, (int)col_len, (int)row_len, matrix, !type);
        }
        return subImage;

    }

    protected Bitmap[] generateAnimation(double[][] coordinates,@Nullable Matrix matrix){

        Bitmap[] animation;

        animation = new Bitmap[coordinates.length];
        for(animation_frame = 0; animation_frame < coordinates.length; animation_frame++ ) {
            animation[animation_frame] = createSubImageAt(
                    coordinates[animation_frame][0]*adapter,
                    coordinates[animation_frame][1]*adapter,
                    coordinates[animation_frame][2]*adapter,
                    coordinates[animation_frame][3]*adapter,
                    matrix,
                    false);
            /*animation[frame] = Bitmap.createScaledBitmap(animation[frame],
                    animation[frame].getWidth()*2,
                    animation[frame].getHeight()*2,
                    true);*/
        }

        return animation;
    }

}
