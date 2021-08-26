___

<p align="center" style="margin-left: 20px;">
  <img src="https://cdn.discordapp.com/attachments/870400219525832795/879425465381707796/icon.png" width="256" height="256" style="display: block;margin-left: auto;margin-right: auto;"/>
</p>

___

## Cubic Skies

Cubic Skies is my second mod, and the first one to change vanilla gameplay instead of just the visuals. The mod's clouds and weather is based on a suggestion by Axoladdy called [Up, Up, and Update: Sunshine Edition](https://new.reddit.com/r/minecraftsuggestions/comments/atvrl4/up_up_and_update_sunshine_edition_a_fully/). I actually always wanted to see the voxel clouds from this suggestion ingame since the first time I saw the suggestion a few years ago, but no one was making UUaU into a mod so I decided to do it myself.

![](https://cdn.discordapp.com/attachments/870400219525832795/879378413293563924/2021-08-22_20.32.42.png)

At their greatest height range—which would be during a thunderstorm—clouds go from normal cloud height to y300 (y192 - y364 in 1.18) meaning that clouds are 172 blocks tall at their max.

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

### Progress/Planned Features.

**Tags:**
- **[FC]**: Probably will get added to Fabulous Clouds instead.
- **[Mod]**: The feature might be different enough from the scope of Fabulous Clouds and Cubic Skies it could become its own mod.
- **[Und]**: I am undecided on if this should go into Fabulous Clouds, be its own mod, or be a part of Cubic Skies. Probably won't be in the first release.
- **[Future]**: Won't get added in the first release but probably will in a future version.

___

**Fixes and Technical Changes:**
- [x] Fix the fog shader.
- [ ] Clouds are server-side instead of client-side.

**Voxel Clouds:**
- [x] Clouds generate in three dimensions now instead of on a plane. 
- [x] Dynamic, procedurally generated cloud shapes based on noise.
- [x] Add cloud chunks so I only have to draw the cloud vertices once. Cloud chunks would be 4 x 57 x 4 cloud voxels large.
- [x] Cloud chunks generate as you move.
- [ ] Cloud cover changes by editing the variables used to generate new clouds. Clouds that are already generated never change shape. 
- [ ] Make cloud shapes look good and base them on [real world cloud types](https://en.wikipedia.org/wiki/List_of_cloud_types).
- [ ] Rain clouds actually make up the entire cloud instead of random cloud voxels being rain clouds.

**Weather Rework:**
- [ ] There are two types of clouds: Rain and Fair clouds.
- [ ] Cloud cover changes what the weather is. You can get a general picture of what the cloud covers will be [here](https://imgur.com/gallery/FLIbgmS), with the main difference being that cloud cover influences weather instead of weather changing cloud cover.
- [ ] Weather is localized based on the number and density of rain clouds in the cloud chunk you're under. The more rain clouds there are the stronger the rain/rain fog. Thunder is based on if your under a thunderstorm cloud chunk.

**Miscellaneous Features:**
- [ ] Randomized wind directions. **[FC]**
- [ ] Cloud color only changes with the time of day (sunsets, night, etc) and doesn't change with weather.
- [ ] The inside of cloud chunks is foggy. Vertices inside cloud chunks get culled.
- [ ] The animation of rain and snow is vivid and angled, not straight down, like in [bedrock edition](https://minecraft.fandom.com/wiki/File:Snowfall_comparsion.gif). **[Und]**
- [ ] Cloud lighting. **[Future]**

### Stuff I have considered adding or am considering adding.

Make a cloudgen api that allows people to edit cloudgen, using either datapacks or by allowing other mods to edit cloudgen. This is definitely going to be a thing in the future, just probably not right now.

Moon phases change cloud and fog color. Would fit better into Fabulous Clouds, but if I end up having to make my own version of vibrant clouds I might add this to both. It could also become a part of a mod that makes the night sky look better. (Starry Night?)

[Lift and Drag status effects](https://imgur.com/gallery/aikw2eg). I have not yet decided on whether I will add these or not.

[Clouds shrink and grow as they move, never intersect with the terrain, pile up against mountains, and burn away over deserts](https://www.reddit.com/r/Minecraft/comments/e7xol/this_is_how_clouds_should_work_gif_simulation/). This would be very laggy because I would have to constantly redraw the cloud vertices as the clouds move over new terrain. There would also be artifacts because of terrain not being generated before suddenly appearing.

The rest of Up, Up, and Update. If I ever do this the rest of UUaU is becoming its own mod.

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