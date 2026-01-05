# Changelog

All notable changes to this project will be documented in this file. See [conventional commits](https://www.conventionalcommits.org/) for commit guidelines.

---
## [0.3.0](https://github.com/gdar463/Minefy/compare/v0.2.0..v0.3.0) - 2026-01-05

### Bug Fixes

- **(neoforge)** fixed MouseClicked event not getting cancelled ([b1b0464](https://github.com/gdar463/Minefy/commit/b1b0464ad4ca6951b68db7147c2cf5185acff95c)) - @gdar463
- readded missing imports for 1.21.1 in PlaybackHUD ([71336b6](https://github.com/gdar463/Minefy/commit/71336b67bc6ac5610fdd76e5763557131cbf3d80)) - @gdar463
- fixed failed mixin injection incompatibility in MouseHandlerMixin ([ce0471e](https://github.com/gdar463/Minefy/commit/ce0471eb72814e014aa7f59795cbad418033dbd3)) - @gdar463
- ensured TextureRenderable doesn't recreate the texture every call ([875211d](https://github.com/gdar463/Minefy/commit/875211d77ccec216b99b2404a9b4cd39e97a008a)) - @gdar463
- made pause/play button invert isPlaying status ([9ae4223](https://github.com/gdar463/Minefy/commit/9ae422335c09d777846f7932119fffe573c2ddc3)) - @gdar463
- fixed crash on neoforge on logging back in ([8588b38](https://github.com/gdar463/Minefy/commit/8588b3886bf3a330bbccab987865af1f4477bfb3)) - @gdar463
- fixed random exception in SpotifyAPI when parsing playlists ([6ecff31](https://github.com/gdar463/Minefy/commit/6ecff31e4fec9eccc5468edc299e55050347c3dc)) - @gdar463
- added util for push/pop matrix and deleted obsolete mixin ([09cf9b7](https://github.com/gdar463/Minefy/commit/09cf9b7f19a8f9f181d607b2b7ae032180b8a7e6)) - @gdar463
- fixed crash on weird track state in PlaybackHUD ([ba48d21](https://github.com/gdar463/Minefy/commit/ba48d2146136b035a3f79e2d5cf4b4a5e1f29fcd)) - @gdar463
- fixed getPlaylists not getting ran in SpotifyUser ([d44b18b](https://github.com/gdar463/Minefy/commit/d44b18bd7d2c9fefbc9c90426d411e65f45f8ecc)) - @gdar463
- fixed release workflow js script ([5072485](https://github.com/gdar463/Minefy/commit/50724854120fb56fdd14a5985987f43fb42ab7b5)) - @gdar463

### Chores

- added library modify scope for auth ([d498a71](https://github.com/gdar463/Minefy/commit/d498a711902854c446cca0ec4f1f4ff271b123c0)) - @gdar463
- added translation keys for all messages ([04f7a71](https://github.com/gdar463/Minefy/commit/04f7a7165e57a140437c1089f46c1fd38b498a6c)) - @gdar463

### Features

- **(sp-api)** added support to add tracks to playlist ([76fedec](https://github.com/gdar463/Minefy/commit/76fedec25d1c27b24509f764482231b6c9258bf1)) - @gdar463
- moved project to stonecutter with modstitch ([2f53ab6](https://github.com/gdar463/Minefy/commit/2f53ab6e3affc0a33b4b4aada08c165d2e2b8f0d)) - @gdar463
- readded actionBar param in ClientUtils sendClientSideMessage ([a67d32e](https://github.com/gdar463/Minefy/commit/a67d32e41d9d2f7677672dee8bea1b91025d5b02)) - @gdar463
- added context to SpotifyPlayer ([c33367a](https://github.com/gdar463/Minefy/commit/c33367af14202f741e737e05c94be8af1c50e6ea)) - @gdar463
- added button to add track to currently playing playlist (for smart shuffle) ([d012070](https://github.com/gdar463/Minefy/commit/d0120707540b7f859783d3b91e25870a4a65da40)) - @gdar463
- added body to login callback page ([54231e6](https://github.com/gdar463/Minefy/commit/54231e6b254436e02e24180424d6af7ce71a8992)) - @gdar463
- abstracted the dynamic texture and download system ([d753507](https://github.com/gdar463/Minefy/commit/d75350773fafcf8b36ca1fa60663142d287f902b)) - @gdar463
- added spotify uri sanitization for ids in Utils ([9c155b2](https://github.com/gdar463/Minefy/commit/9c155b22bc7699be3a461fe30a102505230ef604)) - @gdar463
- abstracted the spotify uri ([1776878](https://github.com/gdar463/Minefy/commit/177687857a423af2384d02cbd56f3b0feefe155e)) - @gdar463
- added playlist support ([c40d245](https://github.com/gdar463/Minefy/commit/c40d2459bc08f89560cfc06e372124bc4a2c5760)) - @gdar463
- added displayName prop on user and playlist ([9eb8325](https://github.com/gdar463/Minefy/commit/9eb8325c775e932a761ea1ee0daeadde5c955d0b)) - @gdar463
- added SaveToPlaylistScreen ([bc1e299](https://github.com/gdar463/Minefy/commit/bc1e299f888993ad3217de620f2be04a8e173d59)) - @gdar463

### Refactors

- made several DX changes ([8672f5e](https://github.com/gdar463/Minefy/commit/8672f5ef10c21e8b8c074f0ff3d5529bf8062d53)) - @gdar463

### Style

- changed SpotifyTrack id to uri ([58e1963](https://github.com/gdar463/Minefy/commit/58e19631b15dcba6a94ffec1c68c765dcc13cd03)) - @gdar463

---
## [0.2.0](https://github.com/gdar463/Minefy/compare/v0.1.0..v0.2.0) - 2025-11-10

Player Buttons Update

### Bug Fixes

- **(sp-api)** skip requests are post not put ([bd8c561](https://github.com/gdar463/Minefy/commit/bd8c561bfe4f0d0f7064d09b947dd9ae59493145)) - @gdar463
- made PlaybackHUD start with client not at world change ([72989b7](https://github.com/gdar463/Minefy/commit/72989b793c1a490e4e63e0d7d653039220c1975d)) - @gdar463
- made user init in initializer not with process token ([e9b50c5](https://github.com/gdar463/Minefy/commit/e9b50c51f4b808ae30aed909c35ade8cb61c7443)) - @gdar463
- set disallows member to 0 when copying in SpotifyPlayer ([b06af58](https://github.com/gdar463/Minefy/commit/b06af58b3e3785347496fff5432f3b38ad435800)) - @gdar463
- fixed SpotifyPlayerDisallows flags being out of real order ([f608c94](https://github.com/gdar463/Minefy/commit/f608c947f037985873ff3ab3dbfe24257a165146)) - @gdar463
- fixed getPlayer spamming the API and getting rate-limited in 3 seconds ([bc61b77](https://github.com/gdar463/Minefy/commit/bc61b773437c664768bc7f929b5075275cd1c4c7)) - @gdar463
- fixed mouseCoords being a valid number when window is null in PlaybackHUD ([472ea84](https://github.com/gdar463/Minefy/commit/472ea84245dfaa1ec9000b4b04081402e96f7b73)) - @gdar463

### Chores

- **(lang)** reorganized lang file ([4580d13](https://github.com/gdar463/Minefy/commit/4580d1358f9009fff2fd6dd4093f7f3635b596de)) - @gdar463
- backported YACL version to 3.7.1 ([e9aace5](https://github.com/gdar463/Minefy/commit/e9aace530b5ef0ab606cdb2187d5fc06f917be4e)) - @gdar463
- added warning when rate limited by spotify in WrapperHttpClient ([c2c4eab](https://github.com/gdar463/Minefy/commit/c2c4eab7a0fb28d88ff1bb394ed68b4fcde0a0fc)) - @gdar463
- made almost all HudConfigCategory groups collapsed by default ([683c51c](https://github.com/gdar463/Minefy/commit/683c51cc54724a17a511f0d663a8f21892b95d45)) - @gdar463

### Features

- **(debug)** added testing items in SpotifyConfigCategory ([64f99d7](https://github.com/gdar463/Minefy/commit/64f99d7751464a68329b59239cc69ddb3fed620e)) - @gdar463
- **(sp-api)** added player actions i.e. resume/pause ([16df306](https://github.com/gdar463/Minefy/commit/16df30630e07ea9584c0cd42c61c42dc0b20b086)) - @gdar463
- **(sp-api)** added seekToPosition call ([9335440](https://github.com/gdar463/Minefy/commit/9335440bcc05fa15c281577a01c5bebf15c6929b)) - @gdar463
- added SpotifyUser to ensure premium user for actions ([578765e](https://github.com/gdar463/Minefy/commit/578765e8780b6988c7d62691491d9ed092462b9d)) - @gdar463
- added check for disallowed actions in SpotifyPlayer ([2146915](https://github.com/gdar463/Minefy/commit/2146915a22e112aa00d43a41cc58c5f1cf5376ae)) - @gdar463
- added new border color when HUD is hovered by cursor ([0f679f4](https://github.com/gdar463/Minefy/commit/0f679f43878ce5e5315922dc6803453efee6ac3f)) - @gdar463
- added resizing of HUD when hovered ([32c2704](https://github.com/gdar463/Minefy/commit/32c27044b838e943b0916cc64a5a126c6def9bae)) - @gdar463
- added buttons when HUD is hovered ([ae56c2e](https://github.com/gdar463/Minefy/commit/ae56c2e4fa6e8b01a2ea7479105c60afa3a3b5a3)) - @gdar463
- added click action to player buttons ([811ed69](https://github.com/gdar463/Minefy/commit/811ed694206eb6ec038f9c3005b5d4074f7b6bff)) - @gdar463
- added updateInterval to SpotifyConfig ([fe5e86a](https://github.com/gdar463/Minefy/commit/fe5e86a29b1fead05d4573b135c334081ec6b6b1)) - @gdar463
- added buttons to config and semi-abstracted their rendering ([349a9bd](https://github.com/gdar463/Minefy/commit/349a9bd637c59a2d6f7a5b8346226e53633b72a3)) - @gdar463
- added coords preset system ([d2fe45c](https://github.com/gdar463/Minefy/commit/d2fe45c01855049edcf6af4a442997a517280ba1)) - @gdar463

### Style

- **(readme)** fixed case typo ([80f111f](https://github.com/gdar463/Minefy/commit/80f111fca1487ae83263dfea5b487a14bed27525)) - @gdar463
- changed text for Spotify not yet linked ([30ce039](https://github.com/gdar463/Minefy/commit/30ce03901bb4ab6872c371e22821bf4ab99bf6c0)) - @gdar463

---
## [0.1.0] - 2025-10-13

Initial Release

