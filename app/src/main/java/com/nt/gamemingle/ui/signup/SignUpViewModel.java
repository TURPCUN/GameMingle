package com.nt.gamemingle.ui.signup;

import android.content.Context;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.nt.gamemingle.ui.common.BaseViewModel;

import java.util.UUID;

public class SignUpViewModel extends BaseViewModel {
    public void signUp(String fullName, String email, String password, Context context, String city) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isComplete()){
                            Toast.makeText(context, "User is registered", Toast.LENGTH_SHORT).show();
                            if (mAuth.getCurrentUser() != null) {
                                saveUserToDatabase(mAuth.getCurrentUser().getUid(), fullName, context, city);
                            }
                        } else {
                            Toast.makeText(context, "User is not registered", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveUserToDatabase(String uuid, String fullName, Context context, String city) {
        databaseReference.child("Users").child(uuid).child("userFullName").setValue(fullName);
        databaseReference.child("Users").child(uuid).child("userCity").setValue(city);
        databaseReference.child("Users").child(uuid).child("registerDate").setValue(System.currentTimeMillis());
    }

    private void tempFeedDatabaseWithGames(){


            UUID uuid = UUID.randomUUID();
            String gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Catan");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(3);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(4);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Catan is a game of discovery, " +
                    "trading and building on the mythical island of Catan. Players take on the roles of settlers, each attempting to build and develop holdings while trading and acquiring resources. Players are rewarded points as their settlements grow; the first to reach a set number of points, typically 10, is the winner. " +
                    "Multi-award-winning and one of the most popular games in recent history due to its amazing ability to appeal to non-gamers and gamers alike.");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://upload.wikimedia.org/wikipedia/en/a/a3/Catan-2015-boxart.jpg");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Strategy");

            uuid = UUID.randomUUID();
            gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Monopoly");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(2);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(8);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Monopoly is a board game currently published by Hasbro. In the game, players roll two six-sided dice to move around the game board, buying and trading properties, and developing them with houses and hotels. Players collect rent from their opponents, with the goal being to drive them into bankruptcy. Money can also be gained or lost through Chance and Community Chest cards, and tax squares; players can end up in jail, which they cannot move from until they have met one of several conditions. The game has numerous house rules, and hundreds of different editions exist, as well as many spin-offs and related media. Monopoly has become a part of international popular culture, having been locally licensed in more than 103 countries and printed in more than 37 languages.");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://staticctf.ubisoft.com/J3yJr34U2pZ2Ieem48Dwy9uqj5PNUQTn/3ZLDuMbssrrbqUg83OY0I6/83974bbb020865f4df323639fbba673a/4CardsSection-Monopoly-348x434.jpg");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Family");

            uuid = UUID.randomUUID();
            gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Ticket to Ride");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(2);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(5);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Ticket to Ride is a railway-themed German-style board game designed by Alan R. Moon. It was illustrated by Julien Delval and Cyrille Daujean and published in 2004 by Days of Wonder. The game is also known as Zug um Zug (German), Les Aventuriers du Rail (French), Aventureros al Tren (Spanish), Wsiąść do pociągu (Polish), and Menolippu (Finnish). The original version of the game is played on a board depicting a railroad map of the world, divided into forty-two territories, which are grouped into six contiguous regions. The object of the game is to score the highest number of total points. Points can be scored by: building the longest continuous series of routes connecting two end points, by fulfilling certain other goals, and by fulfilling the Destination Tickets held at the start of the game and at the end (when the game ends, each Destination Ticket not fulfilled costs the player the number of points printed on the card).");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://upload.wikimedia.org/wikipedia/en/9/92/Ticket_to_Ride_Board_Game_Box_EN.jpg");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Strategy");

            uuid = UUID.randomUUID();
            gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Pandemic");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(2);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(4);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Your team of experts must prevent the world from succumbing to a viral pandemic.");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://m.media-amazon.com/images/I/811YPz8YufL.jpg");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Strategy");

            uuid = UUID.randomUUID();
            gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Carcassonne");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(2);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(5);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Carcassonne is a tile-placement game in which the players draw and place a tile with a piece of southern French landscape on it. The tile might feature a city, a road, a cloister, grassland or some combination thereof, and it must be placed adjacent to tiles that have already been played, in such a way that cities are connected to cities, roads to roads, etcetera");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://cf.geekdo-images.com/okM0dq_bEXnbyQTOvHfwRA__itemrep/img/_GLRhUoVx6Zp4kTE0rv_gi9cyOQ=/fit-in/246x300/filters:strip_icc()/pic6544250.png");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Strategy");

            uuid = UUID.randomUUID();
            gameUuid = uuid.toString();
            databaseReference.child("Games").child(gameUuid).child("gameName").setValue("Codenames");
            databaseReference.child("Games").child(gameUuid).child("minPlayer").setValue(2);
            databaseReference.child("Games").child(gameUuid).child("maxPlayer").setValue(8);
            databaseReference.child("Games").child(gameUuid).child("gameDescription").setValue("Codenames is a 2015 card game for 4–8 players designed by Vlaada Chvátil and published by Czech Games Edition. Two teams compete by each having a Spymaster give one word clues which can point to multiple words on the board. The other players on the team attempt to guess their team's words while avoiding the words of the other team. In a variant with 2–3 players, one Spymaster gives clues to the other player or players.");
            databaseReference.child("Games").child(gameUuid).child("gameImageUrl").setValue("https://cf.geekdo-images.com/F_KDEu0GjdClml8N7c8Imw__itemrep/img/e8zw8YQvQB8q8zfWkHQ48Ls920g=/fit-in/246x300/filters:strip_icc()/pic2582929.jpg");
            databaseReference.child("Games").child(gameUuid).child("gameCategory").setValue("Party");





         }
}
