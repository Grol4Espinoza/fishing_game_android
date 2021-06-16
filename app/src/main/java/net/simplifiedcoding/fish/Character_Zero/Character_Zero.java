package net.simplifiedcoding.fish.Character_Zero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class Character_Zero {

    private Bitmap sprite;
    private int x;
    private int y;
    private int hp;
    private int[] speed;
    private int height;
    private int width;
    private int animation_duration;//cada 60 son 1 segundo, el mobil deberia ir a 60fps
    private int maxY,minY,maxX,minX;
    private final int gravity = 10;
    int screenX, screenY;

    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 70;

    private Rect detectCollision;

    //animation variables------------------------------------------------------------------------------------------------------------------------------------------
    private int animation_type = 0;
    private int state;
    private int animation_frame_index = 0;
    private int counter = -1;
    private int animation_total_frames;
    private  Bitmap current_animation_frame;
    private  Bitmap[] current_animation;
    private Character_Zero_Constants zero_constants;
    private boolean direccion; //false es izquierda, true, derecha
    private int animation_status; // 0 == animation_start, 1 == animation_loop, 2 == animation_end

    public Character_Zero(Context context, int screenX, int screenY, int x, int y){

        this.screenX = screenX;
        this.screenY = screenY;

        this.x = x;
        this.y = y;

        this.zero_constants = new Character_Zero_Constants(context);

        this.resetHp();
        this.speed = new int[]{0,0};

        this.width = zero_constants.iddle_animation_izq[0].getWidth();
        this.height = zero_constants.iddle_animation_izq[0].getHeight();

        this.maxX = this.screenX - this.width;
        this.minX = 0;

        this.maxY = this.screenY - this.height;
        this.minY = 0;

        this.iddle();

        //initializing rect object
        this.detectCollision =  new Rect(x, y, this.width, this.height);

        //initializing current animation
        this.current_animation = this.zero_constants.iddle_animation_izq; //provisional
        this.direccion = true;
        this.animation_duration = 180;
        this.animation_status = 0;


    }

    public void update(){

        this.maxX = this.screenX - this.width;
        this.maxY = this.screenY - this.height;

        this.speed[1] += this.gravity;
        this.x += this.speed[0];
        this.y += this.speed[1];


        if (this.y < this.minY) {
            this.y = this.minY;
        }
        if (this.y > this.maxY) {
            this.y = this.maxY;
            if(this.state == Character_Zero_Constants.en_el_aire) {
                this.iddle();
            }
        }else{
            this.state = Character_Zero_Constants.en_el_aire;
        }

        if (this.x < this.minX) {
            this.x = this.minX;
        }
        if (this.x > this.maxX) {
            this.x = this.maxX;
        }

        if(this.speed[0] < 0){
            direccion = false;
        }else if (this.speed[0] > 0){
            direccion = true;
        }

        this.width = zero_constants.iddle_animation_izq[0].getWidth();
        this.height = zero_constants.iddle_animation_izq[0].getHeight();

        //adding top, left, bottom and right to the rect object
        this.detectCollision.left = this.x;
        this.detectCollision.top = this.y;
        /*detectCollision.right = x + bitmap.getWidth(); //Antes de la animacion
        detectCollision.bottom = y + bitmap.getHeight();*/
        this.detectCollision.right = this.x + this.width;
        this.detectCollision.bottom = this.y + this.height;

        //definimos que frame vamos a dibujar en draw()
        current_animation_frame = this.getCurrentMoveBitmap();
        //para hacer que una animacion dure un segundo a 60fps con n° frames por animacion al indicador frame_index
        // se le suma 1 cada vez que (fps/n°deFrames)%counter == 0, counter indica el numero de frames que han transcurrido
        //definimos si hay que cambiar animaciones de inicio a loop o a end
        this.animation_progress();

    }

    public void draw(Canvas canvas, Paint paint){
        int zero_y = y + (this.height - current_animation_frame.getHeight());
        canvas.drawBitmap(current_animation_frame, x, zero_y, paint);
        // Last draw time.
        //this.lastDrawNanoTime= System.nanoTime();
    }

    public void change_state(int _state){ state = _state;}

    public Rect getDetectCollision() {
        return detectCollision;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Bitmap getBitmap() {
        return getCurrentMoveBitmap();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHp() {return hp;}

    public void addHp(int _hp) { this.hp += _hp; }

    public int[] getSpeed() {
        return speed;
    }

    public void setSpeed(int speedx, int speedy) {
        this.speed = new int[]{speedx, speedy};
    }

    public void gainHp(int _hp) { hp += _hp; }

    public void loseHp(int _hp) { hp -= _hp; }

    public void resetHp() {
        hp = 100;
    }

    public Bitmap[] getMoveBitmaps()  {
        switch (this.state)  {
            case Character_Zero_Constants.quieto_en_el_suelo:
                if(this.hp >= 40){
                    if(!this.direccion){
                        return  this.zero_constants.iddle_animation_izq;
                    } else{
                        return this.zero_constants.iddle_animation_der;
                    }
                }else{
                    if(!this.direccion){
                        return  this.zero_constants.iddle_low_hp_animation_izq;
                    } else{
                        return this.zero_constants.iddle_low_hp_animation_der;
                    }
                }
            case Character_Zero_Constants.moviendote_a_la_izquierda:
                if (animation_status == 0) {
                    return this.zero_constants.walk_start_animation_izq;
                }else if (animation_status == 1) {
                    return this.zero_constants.walk_loop_animation_izq;
                }else if  (animation_status == 2) {
                    return this.zero_constants.walk_loop_transition_to_idle_animation_izq;
                }

            case Character_Zero_Constants.moviendote_a_la_derecha:
                if (animation_status == 0) {
                    return this.zero_constants.walk_start_animation_der;
                }else if (animation_status == 1) {
                    return this.zero_constants.walk_loop_animation_der;
                }else if  (animation_status == 2) {
                    return this.zero_constants.walk_loop_transition_to_idle_animation_der;
                }

            case Character_Zero_Constants.durante_un_dash:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        return this.zero_constants.dash_start_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.dash_loop_animation_izq;
                    }else if  (animation_status == 2) {
                        return this.zero_constants.dash_end_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        return this.zero_constants.dash_start_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.dash_loop_animation_der;
                    }else if  (animation_status == 2) {
                        return this.zero_constants.dash_end_animation_der;
                    }
                }

            case Character_Zero_Constants.durante_ataque_1:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        return this.zero_constants.slash_A_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        return this.zero_constants.slash_A_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.durante_ataque_2:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        return this.zero_constants.slash_B_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        return this.zero_constants.slash_B_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.durante_ataque_3:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        return this.zero_constants.slash_C_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        return this.zero_constants.slash_C_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.en_el_aire:
                if(!this.direccion) {
                    if(this.animation_status == 0){
                        return this.zero_constants.jump_start_izq;
                    }else if(this.speed[1] < -10 ){
                        return this.zero_constants.jump_elevating_loop_izq;
                    }else if(this.speed[1] < 11){
                        return this.zero_constants.jump_start_descending_izq;
                    }else{
                        return this.zero_constants.jump_descending_loop_izq;
                    }
                }else{
                    if(this.animation_status == 0){
                        return this.zero_constants.jump_start_der;
                    }else if(this.speed[1] < -10 ){
                        return this.zero_constants.jump_elevating_loop_der;
                    }else if(this.speed[1] < 11){
                        return this.zero_constants.jump_start_descending_der;
                    }else{
                        return this.zero_constants.jump_descending_loop_der;
                    }

                }

            default:
                return null;
        }
    }

    public Bitmap getCurrentMoveBitmap()  {
        current_animation = this.getMoveBitmaps();
        return current_animation[this.animation_frame_index];
    }

    public void iddle(){

        if(this.state == Character_Zero_Constants.quieto_en_el_suelo){
            //do nothing xD
        }else{
            this.state = Character_Zero_Constants.quieto_en_el_suelo;
            this.animation_frame_index = 0;
            this.animation_duration = 180;
        }
    }

    public void move(){
        if(this.state != Character_Zero_Constants.en_el_aire) {
            if (!this.direccion) {
                this.state = Character_Zero_Constants.moviendote_a_la_izquierda;
                this.setSpeed(-20, this.speed[1]);
                this.animation_frame_index = 0;
                this.animation_duration = 30;
            } else {
                this.state = Character_Zero_Constants.moviendote_a_la_derecha;
                this.setSpeed(20, this.speed[1]);
                this.animation_frame_index = 0;
                this.animation_duration = 30;
            }
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }else{//suponemos que esta en el aire
            if (!this.direccion) {
                this.setSpeed(-20, this.speed[1]);
            } else {
                this.setSpeed(20, this.speed[1]);
            }
        }
    }

    public void jump(){
        this.state = Character_Zero_Constants.en_el_aire;
        this.setSpeed(this.speed[0],-100);
        this.animation_frame_index = 0;
        this.animation_duration = 15;
        this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
    }
    public void dash(){
        this.state = Character_Zero_Constants.durante_un_dash;
        this.setSpeed(MAX_SPEED,this.speed[1]);
        this.animation_frame_index = 0;
        this.animation_duration = 90;
        this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
    }

    public void attack(){
        if(this.state == Character_Zero_Constants.quieto_en_el_suelo ||
                this.state == Character_Zero_Constants.durante_un_dash ||
                this.state == Character_Zero_Constants.moviendote_a_la_derecha ||
                this.state == Character_Zero_Constants.moviendote_a_la_izquierda ||
                (this.state == Character_Zero_Constants.durante_ataque_3 && this.animation_frame_index > 6)){
            this.state = Character_Zero_Constants.durante_ataque_1;
            this.animation_frame_index = 0;
            this.animation_duration = 15;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
        else if(this.state == Character_Zero_Constants.durante_ataque_1 &&
                this.animation_frame_index > 4 ){
            this.state = Character_Zero_Constants.durante_ataque_2;
            this.animation_frame_index = 0;
            this.animation_duration = 15;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
        else if(this.state == Character_Zero_Constants.durante_ataque_2 &&
                this.animation_frame_index > 3){
            this.state = Character_Zero_Constants.durante_ataque_3;
            this.animation_frame_index = 0;
            this.animation_duration = 20;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
    }

    public void die(){

    }


    public void animation_progress(){

        //para hacer que una animacion dure un segundo a 60fps con n° frames por animacion al indicador frame_index
        // se le suma 1 cada vez que counter%(fps/n°deFrames) == 0, counter indica el numero de frames que han transcurrido
        this.counter++;
        /*int anim_dur = this.animation_duration;
        int cur_anim_len = this.current_animation.length;
        Log.i("msj info","anim_dur = "+ anim_dur + " cur_anim_len = "+ cur_anim_len);*/
        if (this.counter%(this.animation_duration / this.current_animation.length) == 0) {
            Log.i("msj info","anim_dur = "+ this.animation_duration + " cur_anim_len = "+ this.current_animation.length+ " this.animation_frame_index = " + this.animation_frame_index);
            this.animation_frame_index++;
            this.counter = 0;
            if(this.animation_frame_index >= this.current_animation.length)  {
                this.animation_frame_index = 0;
                switch (this.state){
                    case Character_Zero_Constants.moviendote_a_la_izquierda:
                    case Character_Zero_Constants.moviendote_a_la_derecha:
                    case Character_Zero_Constants.durante_un_dash:
                    case Character_Zero_Constants.en_el_aire:
                        if(animation_status == 0){
                            this.animation_status++;
                        }
                    case Character_Zero_Constants.durante_ataque_1:
                    case Character_Zero_Constants.durante_ataque_2:
                    case Character_Zero_Constants.durante_ataque_3:
                        if(animation_status == 0){
                            this.animation_status++;
                            this.animation_duration = 10;
                        }else if(animation_status == 1){
                            this.iddle();
                        }

                }
            }
        }
    }

}
