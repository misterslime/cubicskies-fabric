___

<p align="center" style="margin-left: 20px;">
  <img src="https://cdn.discordapp.com/attachments/870400219525832795/879425465381707796/icon.png" width="256" height="256" style="display: block;margin-left: auto;margin-right: auto;"/>
</p>

___

## Cubic Skies

Cubic Skies is my second mod, and the first one to change vanilla gameplay instead of just the visuals. The mod's clouds and weather is based on a suggestion by Axoladdy called [Up, Up, and Update: Sunshine Edition](https://new.reddit.com/r/minecraftsuggestions/comments/atvrl4/up_up_and_update_sunshine_edition_a_fully/). I actually always wanted to see the voxel clouds from this suggestion ingame since the first time I saw the suggestion a few years ago, but no one was making UUaU into a mod so I decided to do it myself.

![](https://cdn.discordapp.com/attachments/870400219525832795/879378413293563924/2021-08-22_20.32.42.png)

At their greatest height range—which would be during a thunderstorm—clouds go from normal cloud height to y300 (y192 - y364 in 1.18) meaning that clouds are 172 blocks tall at their max.

Cubic Skies uses [KdotJPG's OpenSimplex2](https://github.com/KdotJPG/OpenSimplex2) for rain/fair cloud region cell value assignment. OpenSimplex2 is provided under [The Unlicense](https://github.com/KdotJPG/OpenSimplex2/blob/master/UNLICENSE).

### Why not make Cubic Skies a part of Fabulous Clouds instead of it's own mod?

There are many reasons why voxel clouds and the weather rework aren't a part of Fabulous clouds, but here are most of the main ones:
- There are people who like the vanilla 2D clouds and wouldn't like 3D clouds. They might want to have a mostly vanilla experience and think that the features in Cubic Skies isn't vanilla-like.
- Customizable cloud layers would have to be removed. Voxel clouds are already going to take up the entire world above cloud height, meaning there isn't enough space in the minecraft world to add more cloud layers. And generating one layer of voxel clouds is already probably going to be very laggy, I don't want the performance to be doubled by adding voxel cloud layers.
- I'm probably going to have to make my own version of vibrant clouds too because it changes the cloud color to the product of the fog color and the default cloud color, meaning that the rain clouds would be too dark to see during rain because of rain fog.
- Noise clouds function by editing a cloud texture, which would not exist with voxel clouds.
- Voxel clouds cannot be toggleable because voxel clouds would automatically have to disable pretty much all of Fabulous Clouds's features. 
- The weather rework requires voxel clouds to be a thing.
- The weather rework has to be server-side. Otherwise things like the trident, snow cover, and other feature that are affected weather would not work properly. This would lead to bugs like players being able to trident fly when it isn't raining on the server.

The biggest difference between Fabulous Clouds and Cubic Skies is that Fabulous Clouds is not meant to actually change vanilla gameplay and is designed to be entirely client-side so it's able to work on multiplayer without the server adding the mod. Fabulous Clouds is also highly configurable and every feature is toggleable. Cubic Skies changes vanilla gameplay and is both server-side and client-side so both the client and the server have to have to mod installed. Cubic Skies isn't going to have anywhere near as much configurability, and features will not be toggleable.

### Building/Contributing.

Cubic Skies is in its **early development** phase and will be released when it is ready. In the meantime you can build the source code yourself or find a prebuilt version in github actions. Contributing to the project helps make it get done faster. If you want to contribute to the project please do. I have no idea how to do cloudgen.

#### Building
1. Clone the repository.
2. Run `gradlew build` in the project folder.

#### Contributing
1. Clone the repository.

###### IntelliJ Idea
1. Open IntelliJ IDEA.
2. Press Open.
3. Choose the `build.gradle` file and open it as a project.