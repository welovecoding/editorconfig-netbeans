[![](https://travis-ci.org/welovecoding/editorconfig-netbeans.svg?branch=master)](https://travis-ci.org/welovecoding/editorconfig-netbeans)

## :warning: BETA DISCLAIMER

THIS SOFTWARE IS IN BETA AND IS BELIEVED TO CONTAIN DEFECTS. A PRIMARY PURPOSE OF THIS BETA VERSION IS TO OBTAIN FEEDBACK ON SOFTWARE PERFORMANCE AND THE IDENTIFICATION OF DEFECTS. WE DO NOT RECOMMEND TO USE THIS SOFTWARE WITH PRODUCTION CODE. PLEASE MAKE BACKUPS OF IMPORTANT FILES BEFORE USING THIS SOFTWARE; USE CAUTION AND DO NOT TO RELY IN ANY WAY ON THE CORRECT FUNCTIONING OR PERFORMANCE OF THE SOFTWARE AND/OR ACCOMPANYING PARTS.

# EditorConfig NetBeans Plugin


A NetBeans IDE plugin supporting the [EditorConfig][] standard.

**Screenshot**

![Plugin Screenshot](https://dl.dropboxusercontent.com/u/74217418/screenshots/github/editorconfig-plugin/screen-1.png)


## EditorConfig Project

EditorConfig makes it easy to maintain the correct coding style when switching between different text editors and between different projects.  The EditorConfig project maintains a file format and plugins for various text editors which allow this file format to be read and used by those editors. For information on the file format and supported text editors, see the [EditorConfig website][EditorConfig].

## Example file

**.editorconfig**

```ini
# top-most EditorConfig file
root = true

# Unix-style newlines with a newline ending every file
[*]
end_of_line = lf
insert_final_newline = true

# 4 space indentation
[*.py]
indent_style = space
indent_size = 4

# Tab indentation (no size specified)
[*.js]
indent_style = tab

# Indentation override for all JS under lib directory
[lib/**.js]
indent_style = space
indent_size = 2

# Matches the exact files either package.json or .travis.yml
[{package.json,.travis.yml}]
indent_style = space
indent_size = 2
```

## Supported properties

###  :construction:  1. charset

*Values:* `latin1`, `utf-8`, `utf-8-bom`, `utf-16be`, `utf-16le`, `utf-8-bom`

### :white_check_mark: 2. end_of_line

*Values:* `lf`, `cr`, `crlf`

### :white_check_mark: 3. indent_size

*Values:* `[number]`, `tab`

*Special case:*
Indent_size can be set to `tab` if `indent_size` is unspecified and `indent_style` is set to `tab`.
When set to `tab`, the value of `tab_width` (if specified) will be used.

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/Indentation).

### :white_check_mark: 4. indent_style

*Values:* `space`, `tab`

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/Indentation).

### :white_check_mark: 5. insert_final_newline

*Values:* `false`, `true`

### :white_check_mark: 6. tab_width

*Values:* `[number]`

*Special case:*
Defaults to the value of `indent_size` and doesn't usually need to be specified.

Read our notes on [Indentation](https://github.com/welovecoding/editorconfig-netbeans/wiki/Indentation).

### :white_check_mark: 7. trim_trailing_whitespace

*Values:* `false`, `true`

[EditorConfig]: http://editorconfig.org

## Info

- A project must be closed and opened after the plugin is installed, to setup hooks for `.editorconfig` files
- Rules are applied when a file (which is matched by a rule) is saved

## Disclaimer

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT OF THIRD PARTY RIGHTS. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

## Contributors

[![Benny Neugebauer on Stack Exchange][stack_exchange_flair_bennyn]][stack_exchange_link_bennyn]

[![Michael Koppen on Stack Exchange][stack_exchange_flair_yser]][stack_exchange_link_yser]

## Credits

- @geertjanw ([Geertjan Wielenga](https://blogs.oracle.com/geertjan)) for his posts on [EditorConfig and NetBeans IDE](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide) [(Part 2)](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide_part) [(Part 3)](https://blogs.oracle.com/geertjan/entry/editorconfig_and_netbeans_ide_part1)

# Inspiring projects

- [Change line endings Plugin](https://github.com/junichi11/netbeans-change-lf) by @junichi11
- [Encoding Plugin](https://github.com/junichi11/netbeans-encoding-plugin) by @junichi11
- [JSBeautify](https://github.com/drewhamlett/netbeans-jsbeautify) by @drewhamlett


[stack_exchange_link_bennyn]: http://stackexchange.com/users/203782/benny-neugebauer?tab=accounts
[stack_exchange_link_yser]: http://stackexchange.com/users/3210455/yser?tab=accounts
[stack_exchange_flair_bennyn]: http://stackexchange.com/users/flair/203782.png?theme=default
[stack_exchange_flair_yser]: http://stackexchange.com/users/flair/3210455.png?theme=default
