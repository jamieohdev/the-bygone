package com.jamiedev.bygone.common.util;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

/**
  * entities have ongoing synced state! this is a helper that handles a lot of
  * the logic for dealing with that state for the purposes of animations in a
  * clean way. implement Animatable on your entity, write an enum for the states
  * your entity can be in, have the enum implement State, and you're all set!
  *
  * to get the current state: entity.getState()
  *
  * to set the current state: entity.setState()
  *
  * to get the AnimationState for a given state: Entity.State.IDLE.get(entity)
  **/
public interface Animatable<T extends Entity & Animatable<T,S>, S extends Animatable.State<T>>{
    /**
      * just have this return a final field. needed for da magic 2 work
      **/
    Holder<T,S> animations();
    /**
      * returns the EntityDataAccessor storing state data!
      *
      * while we COULD just have our own custom EntityDataSerializer,,, uhh,,,
      * mc only allows 256 of those! total! across all mods!
      * so we will NOT BE DOING THAT.
      **/
    EntityDataAccessor<Integer> getStateData();
    /** no overriding necessary **/
    SynchedEntityData getEntityData();

    /** main user-facing function for getting current state **/
    default S getState(){
        int i = this.getEntityData().get(this.getStateData());
        return this.animations().values[i < this.animations().values.length ? i : 0];
    }
    /** main user-facing function for setting current state **/
    default void setState(S s){
        this.getEntityData().set(this.getStateData(), s.ordinal());
    }



    /**
      * this should be implemented by your state enum! the first enum value is
      * considered the default!!!!!
      **/
    interface State<T extends Entity & Animatable<T,?>>{
        /** no overriding necessary **/
        int ordinal();
        /** main user-facing function for getting a given AnimationState **/
        default AnimationState get(T t){ return t.animations().states[this.ordinal()]; }
    }



    /**
      * this is where the magic happens!
      * init a final field for this, and call onSyncedDataUpdated in your
      * onSyncedDataUpdated (though, make sure your Holder field isn't null!
      * onSyncedDataUpdated is called early), and you'll be set :)
      **/
    class Holder<T extends Entity & Animatable<T,S>, S extends State<T>>{
        private final T parent;
        private final S[] values;
        private final AnimationState[] states;
        public Holder(T parent, S[] values){
            this.parent = parent;
            this.values = values;
            if(this.values.length == 0) throw new IllegalStateException("entity cannot have no animation states???");

            this.states = new AnimationState[this.values.length];
            for(int i = 0; i < this.values.length; i++) this.states[i] = new AnimationState();
            if(this.parent.level().isClientSide) this.states[0].start(this.parent.tickCount);
        }

        public void onSyncedDataUpdated(EntityDataAccessor<?> key){
            if(!parent.getStateData().equals(key)) return;
            // even though this is for syncing server->client, this method is
            // called server-side on sets!
            if(!this.parent.level().isClientSide) return;

            State<T> current = this.parent.getState();
            for(State state : values){
                AnimationState animation = state.get(this.parent);
                if(state == current) animation.startIfStopped(this.parent.tickCount);
                else animation.stop();
            }
        }
    }
}
