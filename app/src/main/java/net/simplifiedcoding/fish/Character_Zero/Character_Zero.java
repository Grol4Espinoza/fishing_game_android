package net.simplifiedcoding.fish.Character_Zero;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private int jump_power = -80;
    int screenX, screenY;

    public int new_movementCounter;
    private int old_movementCounter;
    public boolean movement_status;

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
    private int zero_x = 0;
    private int zero_y = 0;
    private String zeroPositions;

    public Character_Zero(Context context, int screenX, int screenY, int x, int y){

        this.screenX = screenX;
        this.screenY = screenY;

        this.x = x;
        this.y = y;

        this.zero_constants = new Character_Zero_Constants(context);

        this.resetHp();
        this.speed = new int[]{0,0};

        this.width = zero_constants.idle_animation_der[0].getWidth();
        this.height = zero_constants.idle_animation_der[0].getHeight();

        this.maxX = this.screenX - this.width;
        this.minX = 0;

        this.maxY = this.screenY - this.height;
        this.minY = 0;

        this.idle();

        //initializing rect object
        this.detectCollision =  new Rect(this.x, this.y, this.width, this.height);

        //initializing current animation
        this.current_animation = this.zero_constants.jump_descending_loop_der; //provisional
        this.direccion = true;
        this.animation_duration = 30;
        this.animation_status = 0;

        this.old_movementCounter = 0;
        this.new_movementCounter = 0;
        this.movement_status = false;

    }

    public void update(){

        this.updateState();
        this.updateFrame();
        this.updatePosition();
        this.updateAnimation();

    }

    private void updateState(){

        //verifica si estoy cayendo
        if (this.y >= this.maxY) {
            this.y = this.maxY;
            if(this.state == Character_Zero_Constants.en_el_aire) {// el pj esta cayendo, choca con el suelo, pasa a idle anim.
                this.idle();
            }
        }else{ // el pj no ha chocado con una superficie, entonces esta cayendo
            this.state = Character_Zero_Constants.en_el_aire;
        }

        //verifica en que direccion me muevo
        if(this.speed[0] < 0){//velocidad a la izq
            direccion = false; //seteamos la direccion
            if(this.state == Character_Zero_Constants.quieto_en_el_suelo){//esta en en suelo y mov a la izq, entonces esta caminando
                this.move(false);
            }
        }else if (this.speed[0] > 0){
            direccion = true;
            if(this.state == Character_Zero_Constants.quieto_en_el_suelo){//esta en en suelo y mov a la der, entonces esta caminando
                this.move(true);
            }
        }

        //verifica si el pj dejo de moverce horizontalmente
        if(this.old_movementCounter == this.new_movementCounter && (this.state == Character_Zero_Constants.moviendote_a_la_derecha || this.state == Character_Zero_Constants.moviendote_a_la_izquierda) ){
            //Log.i("comprobando movimiento","dejo de moverse");
            this.movement_status = false;
            idle();
        }
        this.old_movementCounter = this.new_movementCounter;

    }

    private void updateFrame(){

        //definimos que frame vamos a dibujar en draw()
        current_animation_frame = this.getCurrentMoveBitmap();

    }

    private void updatePosition(){

        //actualizamos width y height del pj
        if(this.state == Character_Zero_Constants.en_el_aire) {//si esta en el aire usamos el tamaño por defecto
            this.width = zero_constants.idle_animation_der[0].getWidth();
            this.height = zero_constants.idle_animation_der[0].getHeight();
        }else {
            if(this.state == Character_Zero_Constants.durante_ataque_1 && this.animation_status == 0){ //durante los ataques usamos los width y height del pj dentro de cada frame
                this.width = (int) (Character_Zero_Constants.slash_A_zero[animation_frame_index][2] * this.zero_constants.adapter);
                this.height = (int) (Character_Zero_Constants.slash_A_zero[animation_frame_index][3] * this.zero_constants.adapter);
            }else if(this.state == Character_Zero_Constants.durante_ataque_2 && this.animation_status == 0){
                this.width = (int) (Character_Zero_Constants.slash_B_zero[animation_frame_index][2] * this.zero_constants.adapter);
                this.height = (int) (Character_Zero_Constants.slash_B_zero[animation_frame_index][3] * this.zero_constants.adapter);
            }else if(this.state == Character_Zero_Constants.durante_ataque_3 && this.animation_status == 0){
                this.width = (int) (Character_Zero_Constants.slash_C_zero[animation_frame_index][2] * this.zero_constants.adapter);
                this.height = (int) (Character_Zero_Constants.slash_C_zero[animation_frame_index][3] * this.zero_constants.adapter);
            }else {
                this.width = current_animation_frame.getWidth();
                this.height = current_animation_frame.getHeight();
            }
        }

        //nuevos maxX y maxY
        this.maxX = this.screenX - this.width;
        this.maxY = this.screenY-500 - this.height;

        //actualizamos la velocidad
        this.speed[1] += gravity;
        //actualizamos las posiciones
        this.x += this.speed[0];
        this.y += this.speed[1];

        //vemos que no se pasen de los limites
        if (this.x <= this.minX) {
            this.x = this.minX;
        }
        if (this.x >= this.maxX) {
            this.x = this.maxX;
        }
        if (this.y <= this.minY){
            this.y = this.minY;
        }
        if (this.y >= this.maxY) {
            this.y = this.maxY;
        }

        //actualizamos el hitbox del pj
        this.detectCollision.left = this.x;
        this.detectCollision.top = this.y;
        this.detectCollision.right = this.x + this.width;
        this.detectCollision.bottom = this.y + this.height;

        this.zeroPositions = this.GetZeroPosition();
        Log.i("zeroPositions",this.zeroPositions);
    }

    private String GetZeroPosition(){
        this.zero_x = this.x;
        this.zero_y = this.y;
        switch(this.state){
            case Character_Zero_Constants.durante_ataque_1:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter * (Character_Zero_Constants.slash_A[animation_frame_index][2] - ( Character_Zero_Constants.slash_A_zero[animation_frame_index][2] + Character_Zero_Constants.slash_A_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }else{
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }

            case Character_Zero_Constants.durante_ataque_2:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter *  (Character_Zero_Constants.slash_B[animation_frame_index][2] - ( Character_Zero_Constants.slash_B_zero[animation_frame_index][2] + Character_Zero_Constants.slash_B_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }else{
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }

            case Character_Zero_Constants.durante_ataque_3:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter * (Character_Zero_Constants.slash_C[animation_frame_index][2] - ( Character_Zero_Constants.slash_C_zero[animation_frame_index][2] + Character_Zero_Constants.slash_C_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }else{
                    if (animation_status == 0) {
                        this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][1]);
                        return "zero_x = "+this.zero_x+" zero_y = "+this.zero_y;
                    }
                }
                default:
                return "no estas en estado de ataque";
        }

    }

    private void updateAnimation(){
        this.animation_progress();
    }

    public void draw(Canvas canvas, Paint paint){
        //Log.i("msj info","state = "+ this.zero_constants.printState(this.state) + " anim_dur = "+ this.animation_duration + " cur_anim_len = "+ this.current_animation.length+ " this.animation_frame_index = " + this.animation_frame_index+ " this.animation_status = " + this.animation_status);

        //zero_y = y + (this.height - current_animation_frame.getHeight());

        Paint zero_frame = new Paint();
        zero_frame.setColor(Color.rgb(102, 102, 255));
        zero_frame.setStrokeWidth(0);

        Paint zero_box = new Paint();
        zero_box.setColor(Color.rgb(203, 118, 121));
        zero_box.setStrokeWidth(0);

        Paint line = new Paint();
        line.setColor(Color.rgb(0, 0, 0));
        line.setStrokeWidth(0);



        canvas.drawRect(x, y, x+width, y+height, zero_frame);
        canvas.drawLine(0,screenY-500,3000,screenY-500,line);

        Log.i("Draw coord","x = "+x+" y = "+y+" width = "+width+" height = "+height+" test 500 = "+(y+height));
        canvas.drawBitmap(current_animation_frame, zero_x, zero_y, paint);

        /*if(this.state == Character_Zero_Constants.durante_ataque_1 ||
                this.state == Character_Zero_Constants.durante_ataque_2 ||
                this.state == Character_Zero_Constants.durante_ataque_3) {
            //Log.i("check coord","attack coord");
            canvas.drawBitmap(current_animation_frame, zero_x, zero_y, paint);
        }else{
            canvas.drawBitmap(current_animation_frame, x, y, paint);
        }*/
        //canvas.drawBitmap(current_animation_frame, x, zero_y, paint);
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
                        return  this.zero_constants.idle_animation_izq;
                    } else{
                        return this.zero_constants.idle_animation_der;
                    }
                }else{
                    if(!this.direccion){
                        return  this.zero_constants.idle_low_hp_animation_izq;
                    } else{
                        return this.zero_constants.idle_low_hp_animation_der;
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
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter * (Character_Zero_Constants.slash_A[animation_frame_index][2] - ( Character_Zero_Constants.slash_A_zero[animation_frame_index][2] + Character_Zero_Constants.slash_A_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_A_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_A_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_A_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.durante_ataque_2:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter *  (Character_Zero_Constants.slash_B[animation_frame_index][2] - ( Character_Zero_Constants.slash_B_zero[animation_frame_index][2] + Character_Zero_Constants.slash_B_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_B_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_B_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_B_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.durante_ataque_3:
                if(!this.direccion) {
                    if (animation_status == 0) {
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter * (Character_Zero_Constants.slash_C[animation_frame_index][2] - ( Character_Zero_Constants.slash_C_zero[animation_frame_index][2] + Character_Zero_Constants.slash_C_zero[animation_frame_index][0])));
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_C_animation_izq;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_izq;
                    }
                }else{
                    if (animation_status == 0) {
                        /*this.zero_x = (int) (this.x - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][0]);
                        this.zero_y = (int) (this.y - this.zero_constants.adapter * Character_Zero_Constants.slash_C_zero[animation_frame_index][1]);*/
                        return this.zero_constants.slash_C_animation_der;
                    }else if (animation_status == 1) {
                        return this.zero_constants.slash_End_animation_der;
                    }
                }

            case Character_Zero_Constants.en_el_aire:
                if(!this.direccion) {
                    if(this.animation_status == 0 && speed[1] < 0){
                        return this.zero_constants.jump_start_izq;
                    }else if(this.speed[1] <  - this.gravity - 1 ){
                        return this.zero_constants.jump_elevating_loop_izq;
                    }else if(this.speed[1] < this.gravity + 1){
                        return this.zero_constants.jump_start_descending_izq;
                    }else{
                        return this.zero_constants.jump_descending_loop_izq;
                    }
                }else{
                    if(this.animation_status == 0 && speed[1] < 0){
                        return this.zero_constants.jump_start_der;
                    }else if(this.speed[1] <  - this.gravity - 1 ){
                        return this.zero_constants.jump_elevating_loop_der;
                    }else if(this.speed[1] < this.gravity + 1){
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
        if (this.animation_frame_index >= this.current_animation.length)
            return this.current_animation[this.current_animation.length - 1];
        else
            return this.current_animation[this.animation_frame_index];

    }

    public void idle(){

        this.setSpeed(0,0);

        if(this.state == Character_Zero_Constants.quieto_en_el_suelo){
            //do nothing xD
        }else{
            this.state = Character_Zero_Constants.quieto_en_el_suelo;
            this.animation_frame_index = 0;
            this.animation_duration = 100;
            this.animation_status = 0;
        }
    }

    public void move(boolean _direccion){
        this.direccion = _direccion;
        if(this.state != Character_Zero_Constants.en_el_aire) {
            if (!this.direccion) {
                this.state = Character_Zero_Constants.moviendote_a_la_izquierda;
                this.setSpeed(-20, this.speed[1]);
                if(!this.movement_status) {
                    this.animation_frame_index = 0;
                    this.animation_duration = 2;
                    //Log.i("comprobando movimiento","reiniciando animation status");
                    this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
                }
            } else {
                this.state = Character_Zero_Constants.moviendote_a_la_derecha;
                this.setSpeed(20, this.speed[1]);
                if(!this.movement_status) {
                    this.animation_frame_index = 0;
                    this.animation_duration = 2;
                    //Log.i("comprobando movimiento","reiniciando animation status");
                    this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
                }
            }

        }else{//suponemos que esta en el aire
            if (!this.direccion) {
                this.setSpeed(-20, this.speed[1]);
            } else {
                this.setSpeed(20, this.speed[1]);
            }
        }
    }

    public void jump(){
        this.jump(this.jump_power);
    }

    public void jump(int _jump_power){
        this.state = Character_Zero_Constants.en_el_aire;
        this.jump_power = _jump_power;
        this.setSpeed(this.speed[0],this.jump_power);
        this.animation_frame_index = 0;
        this.animation_duration = 9;
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

            this.setSpeed(0,0);

            this.animation_frame_index = 0;
            this.animation_duration = 15;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
        else if((this.state == Character_Zero_Constants.durante_ataque_1 &&
                this.animation_frame_index > 4) ||
                (this.state == Character_Zero_Constants.durante_ataque_1 &&
                        this.animation_status == 1)){
            this.state = Character_Zero_Constants.durante_ataque_2;

            this.setSpeed(0,0);

            this.animation_frame_index = 0;
            this.animation_duration = 15;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
        else if((this.state == Character_Zero_Constants.durante_ataque_2 &&
                this.animation_frame_index > 3) ||
                (this.state == Character_Zero_Constants.durante_ataque_2 &&
                        this.animation_status == 1)){
            this.state = Character_Zero_Constants.durante_ataque_3;

            this.setSpeed(0,0);

            this.animation_frame_index = 0;
            this.animation_duration = 20;
            this.animation_status = 0;// reiniciamos el status de la animacion para que recorra el estado start y loop
        }
    }

    public void die(){

    }


    private void animation_progress(){

        //para hacer que una animacion dure un segundo a 60fps con n° frames por animacion al indicador frame_index
        // se le suma 1 cada vez que counter%(fps/n°deFrames) == 0, counter indica el numero de frames que han transcurrido
        this.counter++;
        /*int anim_dur = this.animation_duration;
        int cur_anim_len = this.current_animation.length;
        Log.i("msj info","anim_dur = "+ anim_dur + " cur_anim_len = "+ cur_anim_len);*/
        int _progress = this.animation_duration / this.current_animation.length;
        if (_progress == 0){
            _progress = 1;
        }
        int progress = this.counter%(_progress);
        if ( progress == 0) {
            //Log.i("msj info","state = "+ this.zero_constants.printState(this.state) + " anim_dur = "+ this.animation_duration + " cur_anim_len = "+ this.current_animation.length+ " this.animation_frame_index = " + this.animation_frame_index+ " this.animation_status = " + this.animation_status);
            this.animation_frame_index++;
            this.counter = 0;
            //Log.i("msj info","this.animation_frame_index = " + this.animation_frame_index + " this.current_animation.length = " + this.current_animation.length);
            if(this.animation_frame_index >= this.current_animation.length)  {
                this.animation_frame_index = 0;
                switch (this.state){
                    case Character_Zero_Constants.durante_un_dash:
                    case Character_Zero_Constants.en_el_aire:
                        if(animation_status == 0){
                            this.animation_status = 1;
                            this.animation_duration = 30;
                        }
                        break;
                    case Character_Zero_Constants.durante_ataque_1:
                    case Character_Zero_Constants.durante_ataque_2:
                    case Character_Zero_Constants.durante_ataque_3:
                        if(animation_status == 0){//termina la animacion de ataque
                            this.animation_status++;//inicia la animacion de envainar la espada
                            this.animation_duration = 10;//duracion de frames de la animacion
                            Log.i("msj info","envaina espada");
                        }else if(animation_status == 1){
                            Log.i("msj info","termino envaine");
                            this.idle();
                        }
                        break;
                    case Character_Zero_Constants.moviendote_a_la_izquierda:
                    case Character_Zero_Constants.moviendote_a_la_derecha:
                        if(animation_status == 0){
                            this.animation_status = 1;
                            this.animation_duration = 10;
                            //Log.i("msj info","corriendo cambia de animacion");

                        }
                        break;

                }
            }
        }
    }

    public void scalarProductMat (double scalar, double[][] array){
        for (int i=0;i<array.length;i++){
            for (int j=0;j<array[i].length;j++){
                array[i][j] = scalar * array[i][j];
            }
        }
    }

}
