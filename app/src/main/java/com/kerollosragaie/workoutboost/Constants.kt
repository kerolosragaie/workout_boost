package com.kerollosragaie.workoutboost

import com.kerollosragaie.workoutboost.models.Exercise

object Constants {
    fun defaultExerciseList(): ArrayList<Exercise> {

        return arrayListOf(
            Exercise(
                id = 1,
                name = "Jumping jacks",
                image = R.drawable.iv_jumping_jacks,
                practisingTime = 6,
            ),
            Exercise(
                id = 2,
                name = "Abdominal crunch",
                image = R.drawable.iv_abdominal_crunch,
            ),

            Exercise(
                id = 3,
                name = "High knees running in place",
                image = R.drawable.iv_high_knees_running_in_place,
                practisingTime = 6,
            ),

            Exercise(
                id = 4,
                name = "Lunge",
                image = R.drawable.iv_lunge,
            ),
            Exercise(
                id = 5,
                name = "Plank",
                image = R.drawable.iv_plank,
            ),
            Exercise(
                id = 6,
                name = "Push up",
                image = R.drawable.iv_push_up,
                practisingTime = 6,
            ),
            Exercise(
                id = 7,
                name = "Push up and rotation",
                image = R.drawable.iv_push_up_and_rotation,
            ),
            Exercise(
                id = 8,
                name = "Side plank",
                image = R.drawable.iv_side_plank,
            ),
            Exercise(
                id = 9,
                name = "Squat",
                image = R.drawable.iv_squat,
                practisingTime = 6,
            ),
            Exercise(
                id = 10,
                name = "Step up onto chair",
                image = R.drawable.iv_step_up_onto_chair,
            ),
            Exercise(
                id = 11,
                name = "Triceps clip on chair",
                image = R.drawable.iv_triceps_dip_on_chair,
                practisingTime = 6,
            ),
            Exercise(
                id = 12,
                name = "Wall sit",
                image = R.drawable.iv_wall_sit,
                practisingTime = 6,
            ),
        )
    }
}