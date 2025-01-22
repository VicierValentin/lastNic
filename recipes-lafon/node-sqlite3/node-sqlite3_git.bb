# Recipe created by recipetool
# This is the basis of a recipe and may need further editing in order to be fully functional.
# (Feel free to remove these comments when editing.)

SUMMARY = "Asynchronous, non-blocking SQLite3 bindings"
HOMEPAGE = "https://github.com/mapbox/node-sqlite3"
# WARNING: the following LICENSE and LIC_FILES_CHKSUM values are best guesses - it is
# your responsibility to verify that the values are complete and correct.
#
# The following license files were not able to be identified and are
# represented as "Unknown" below, you will need to check them yourself:
#   LICENSE
#   node_modules/isstream/LICENSE.md
#   node_modules/tweetnacl/LICENSE
#   node_modules/aws-sign2/LICENSE
#   node_modules/node-addon-api/LICENSE.md
#   node_modules/glob/LICENSE
#   node_modules/bcrypt-pbkdf/LICENSE
#   node_modules/fs.realpath/LICENSE
#   node_modules/detect-libc/LICENSE
#   node_modules/string_decoder/LICENSE
#   node_modules/dashdash/LICENSE.txt
#   node_modules/request/LICENSE
#   node_modules/process-nextick-args/license.md
#   node_modules/tunnel-agent/LICENSE
#   node_modules/abbrev/LICENSE
#   node_modules/block-stream/LICENCE
#   node_modules/forever-agent/LICENSE
#   node_modules/rc/LICENSE.BSD
#   node_modules/rc/LICENSE.APACHE2
#   node_modules/minizlib/LICENSE
#   node_modules/oauth-sign/LICENSE
#   node_modules/balanced-match/LICENSE.md
#   node_modules/sax/LICENSE
#   node_modules/jsbn/LICENSE
#   node_modules/readable-stream/LICENSE
#   node_modules/tough-cookie/LICENSE
#   node_modules/extsprintf/LICENSE
#   node_modules/verror/LICENSE
#   node_modules/asn1/LICENSE
#   node_modules/jsprim/LICENSE
#   node_modules/caseless/LICENSE
#   node_modules/ecc-jsbn/lib/LICENSE-jsbn
#   node_modules/node-pre-gyp/LICENSE
#   node_modules/node-gyp/gyp/LICENSE
#   node_modules/qs/LICENSE
#
# NOTE: multiple licenses have been detected; they have been separated with &
# in the LICENSE value for now since it is a reasonable assumption that all
# of the licenses apply. If instead there is a choice between the multiple
# licenses then you should change the value to separate the licenses with |
# instead of &. If there is any doubt, check the accompanying documentation
# to determine which situation is applicable.
LICENSE = "Unknown & ISC & MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=79558839a9db3e807e4ae6f8cd100c1c \
                    file://node_modules/wrappy/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/mime-db/LICENSE;md5=c8d3a30332ecb31cfaf4c0a06da18f5c \
                    file://node_modules/har-validator/LICENSE;md5=6f7daec5e5143ffeb21745646b425ab2 \
                    file://node_modules/wide-align/LICENSE;md5=9d215c9223fbef14a4642cc450e7ed4b \
                    file://node_modules/form-data/License;md5=7aa505292e2636a9e59d4d3f258f4819 \
                    file://node_modules/which/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/minimatch/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/is-fullwidth-code-point/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/isstream/LICENSE.md;md5=1798150b9d70250c42b55b3530e6af2b \
                    file://node_modules/tweetnacl/LICENSE;md5=911690f51af322440237a253d695d19f \
                    file://node_modules/aws-sign2/LICENSE;md5=f3f8ead5440d1c311b45be065d135d90 \
                    file://node_modules/graceful-fs/LICENSE;md5=fd63805fd8e3797063b247781e5ee6e4 \
                    file://node_modules/node-addon-api/LICENSE.md;md5=0492ef29a9d558a3e9660e7accc9ca6a \
                    file://node_modules/safer-buffer/LICENSE;md5=3baebc2a17b8f5bff04882cd0dc0f76e \
                    file://node_modules/delegates/License;md5=039225978c07bc42e8c0ef2f72b81c09 \
                    file://node_modules/glob/LICENSE;md5=c727d36f28f2762b1011dd483aa1a191 \
                    file://node_modules/yallist/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/code-point-at/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/bcrypt-pbkdf/LICENSE;md5=aaf6ebb9d1b815768d32661e72bdd761 \
                    file://node_modules/once/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/fs.realpath/LICENSE;md5=062470525c8e380f8567f665ef554d11 \
                    file://node_modules/json-stringify-safe/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/os-tmpdir/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/are-we-there-yet/LICENSE;md5=039a23da29f56411e6d75d7b7bc9de13 \
                    file://node_modules/set-blocking/LICENSE.txt;md5=8fd106383180f7bbb8f534414fdf7d35 \
                    file://node_modules/ansi-regex/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/detect-libc/LICENSE;md5=e3fc50a88d0a364313df4b21ef20c29e \
                    file://node_modules/console-control-strings/LICENSE;md5=43abbc6f9093aea69560715033788727 \
                    file://node_modules/npm-normalize-package-bin/LICENSE;md5=89966567781ee3dc29aeca2d18a59501 \
                    file://node_modules/fast-deep-equal/LICENSE;md5=ea87ade09b9e6da4f2e47904a4ee137b \
                    file://node_modules/signal-exit/LICENSE.txt;md5=e29e20260a1c78dba16a233048565cde \
                    file://node_modules/fstream/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/npm-bundled/LICENSE;md5=ff53df3ad94e5c618e230ab49ce310fa \
                    file://node_modules/strip-ansi/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/ms/license.md;md5=fd56fd5f1860961dfa92d313167c37a6 \
                    file://node_modules/mime-types/LICENSE;md5=bf1f9ad1e2e1d507aef4883fff7103de \
                    file://node_modules/string_decoder/LICENSE;md5=14af51f8c0a6c6e400b53e18c6e5f85c \
                    file://node_modules/mkdirp/LICENSE;md5=b2d989bc186e7f6b418a5fdd5cc0b56b \
                    file://node_modules/aws4/LICENSE;md5=ff6f181928a91d8631d565516b4db3cb \
                    file://node_modules/path-is-absolute/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/dashdash/LICENSE.txt;md5=ebce61c1a0900d798daaf5853c69ce72 \
                    file://node_modules/combined-stream/License;md5=5270b4ddb6700d0a820e6d066744589c \
                    file://node_modules/util-deprecate/LICENSE;md5=b7c99ef4b0f3ad9911a52219947f8cf0 \
                    file://node_modules/request/LICENSE;md5=f3f8ead5440d1c311b45be065d135d90 \
                    file://node_modules/psl/LICENSE;md5=2425d288296fae32e27553694ff40294 \
                    file://node_modules/har-schema/LICENSE;md5=bfa81591facc9224e4c7451c8dc12958 \
                    file://node_modules/ini/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/concat-map/LICENSE;md5=aea1cde69645f4b99be4ff7ca9abcce1 \
                    file://node_modules/process-nextick-args/license.md;md5=216769dac98a78ec088ee7cc6fad1dfa \
                    file://node_modules/tunnel-agent/LICENSE;md5=f3f8ead5440d1c311b45be065d135d90 \
                    file://node_modules/abbrev/LICENSE;md5=e9c0b639498fbe60d17b10099aba77c0 \
                    file://node_modules/block-stream/LICENCE;md5=c695aba12d799c74e770d90e1c5d59aa \
                    file://node_modules/block-stream/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/forever-agent/LICENSE;md5=f3f8ead5440d1c311b45be065d135d90 \
                    file://node_modules/sshpk/LICENSE;md5=38ecf0a3a3894f404ca99c2a08924afc \
                    file://node_modules/punycode/LICENSE-MIT.txt;md5=ee9bd8b835cfcd512dd644540dd96987 \
                    file://node_modules/nopt/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/json-schema-traverse/LICENSE;md5=ea87ade09b9e6da4f2e47904a4ee137b \
                    file://node_modules/needle/license.txt;md5=2fd561ba82cb42a5a9304f08b2506da4 \
                    file://node_modules/delayed-stream/License;md5=5270b4ddb6700d0a820e6d066744589c \
                    file://node_modules/performance-now/license.txt;md5=a07ae1b6dc33215d89a1281ee71c863c \
                    file://node_modules/tar/LICENSE;md5=34c65f5b9b91f31827910d3b54bd6497 \
                    file://node_modules/rc/LICENSE.BSD;md5=e7a2a325a0069e82aff675bbf74464a0 \
                    file://node_modules/rc/LICENSE.APACHE2;md5=ffcf739dca268cb0f20336d6c1a038f1 \
                    file://node_modules/rc/LICENSE.MIT;md5=e0f70a42adf526e6f5e605a94d98a420 \
                    file://node_modules/minipass/LICENSE;md5=ff53df3ad94e5c618e230ab49ce310fa \
                    file://node_modules/http-signature/LICENSE;md5=38ecf0a3a3894f404ca99c2a08924afc \
                    file://node_modules/asynckit/LICENSE;md5=177bc287fb9558bf3ea50b440c1c86ff \
                    file://node_modules/gauge/LICENSE;md5=43abbc6f9093aea69560715033788727 \
                    file://node_modules/minizlib/LICENSE;md5=d8a0ca0c46bfa01db064fa836f550966 \
                    file://node_modules/oauth-sign/LICENSE;md5=f3f8ead5440d1c311b45be065d135d90 \
                    file://node_modules/fs-minipass/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/aproba/LICENSE;md5=9d215c9223fbef14a4642cc450e7ed4b \
                    file://node_modules/inflight/LICENSE;md5=90a3ca01a5efed8b813a81c6c8fa2e63 \
                    file://node_modules/core-util-is/LICENSE;md5=6126e36127d20ec0e2f637204a5c68ff \
                    file://node_modules/balanced-match/LICENSE.md;md5=7fa99ddc3424107350ca6e9a24552085 \
                    file://node_modules/object-assign/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/minimist/LICENSE;md5=aea1cde69645f4b99be4ff7ca9abcce1 \
                    file://node_modules/sax/LICENSE;md5=326d5674181c4bb210e424772c60fa80 \
                    file://node_modules/npm-packlist/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/safe-buffer/LICENSE;md5=badd5e91c737e7ffdf10b40c1f907761 \
                    file://node_modules/isexe/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/jsbn/LICENSE;md5=c6ea389b6b52325149beeec27075c5ac \
                    file://node_modules/uuid/LICENSE.md;md5=c9420736e23afb538ba5fbe44197f0b4 \
                    file://node_modules/has-unicode/LICENSE;md5=2bab5b1c26e9c44fc4e489bb98cfb196 \
                    file://node_modules/readable-stream/LICENSE;md5=a67a7926e54316d90c14f74f71080977 \
                    file://node_modules/iconv-lite/LICENSE;md5=f942263d98f0d75e0e0101884e86261d \
                    file://node_modules/strip-json-comments/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/tough-cookie/LICENSE;md5=7189377a5215f1211b70cf2b9754841e \
                    file://node_modules/ignore-walk/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/extsprintf/LICENSE;md5=bc3c23d98d7aa86bbf232058884e19b2 \
                    file://node_modules/fast-json-stable-stringify/LICENSE;md5=febe55307df96f60ad763842f5a8ca6f \
                    file://node_modules/ajv/LICENSE;md5=4973982316cdc12e988b814af2813df7 \
                    file://node_modules/semver/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/verror/LICENSE;md5=04fce49dd88d841ceb36616ab9789ce0 \
                    file://node_modules/npmlog/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/getpass/LICENSE;md5=38ecf0a3a3894f404ca99c2a08924afc \
                    file://node_modules/asn1/LICENSE;md5=5ce2f42143c298495637e28e40cd5462 \
                    file://node_modules/jsprim/LICENSE;md5=bc3c23d98d7aa86bbf232058884e19b2 \
                    file://node_modules/caseless/LICENSE;md5=e9dadf023ba6ebd98e3e0acb6e2470e3 \
                    file://node_modules/deep-extend/LICENSE;md5=827bb5781213ff1e9d2fe309bbfc0115 \
                    file://node_modules/brace-expansion/LICENSE;md5=a5df515ef062cc3affd8c0ae59c059ec \
                    file://node_modules/osenv/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/inherits/LICENSE;md5=5b2ef2247af6d355ae9d9f988092d470 \
                    file://node_modules/ecc-jsbn/LICENSE;md5=0d289b6d63cbcc18e4b2af9a5bd28b54 \
                    file://node_modules/ecc-jsbn/lib/LICENSE-jsbn;md5=32052d48bacabd1c02e56c6889082a27 \
                    file://node_modules/extend/LICENSE;md5=33d9f7a91df276ec16aa941032ee1476 \
                    file://node_modules/number-is-nan/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/os-homedir/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/is-typedarray/LICENSE.md;md5=aea1cde69645f4b99be4ff7ca9abcce1 \
                    file://node_modules/node-pre-gyp/LICENSE;md5=7e13c3cf883a44ebcc74a8f568c0f6fb \
                    file://node_modules/node-pre-gyp/node_modules/nopt/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/node-pre-gyp/node_modules/tar/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/rimraf/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/node-gyp/LICENSE;md5=694e396551033371686c80d3a1a69e88 \
                    file://node_modules/node-gyp/gyp/LICENSE;md5=ab828cb8ce4c62ee82945a11247b6bbd \
                    file://node_modules/string-width/license;md5=a12ebca0510a773644101a99a867d210 \
                    file://node_modules/chownr/LICENSE;md5=82703a69f6d7411dde679954c2fd9dca \
                    file://node_modules/debug/LICENSE;md5=ddd815a475e7338b0be7a14d8ee35a99 \
                    file://node_modules/qs/LICENSE;md5=d5c7c6dc45a33a0a9620ed81315672d7 \
                    file://package.json;md5=e14f66c2163f8e9e63d0c2e799fafb46 \
                    file://node_modules/abbrev/package.json;md5=09144e5559c19012a5ad2b1cb548f188 \
                    file://node_modules/ajv/package.json;md5=705204b78879f3585fb810d1e2a0c1d1 \
                    file://node_modules/ansi-regex/package.json;md5=fbd3a8909a47de214c7288046091267c \
                    file://node_modules/aproba/package.json;md5=617442ed4770d06ac955969e7dcf7b98 \
                    file://node_modules/are-we-there-yet/package.json;md5=386ebd73fb9d607ee175b947082764ef \
                    file://node_modules/asn1/package.json;md5=0c5153fa7dfd857870bc28c3bacf921d \
                    file://node_modules/assert-plus/package.json;md5=2b7930e340a2439c18ca9871c3dbb834 \
                    file://node_modules/asynckit/package.json;md5=e1fcbbcbd664b4b2966883624fbad801 \
                    file://node_modules/aws-sign2/package.json;md5=f4b8ff99a798371a0647cdc9243723f6 \
                    file://node_modules/aws4/package.json;md5=bf319fd6ee435df1abeb9f9edd0c336d \
                    file://node_modules/balanced-match/package.json;md5=a2181cb8cab70df2084e1bdce79a389b \
                    file://node_modules/bcrypt-pbkdf/package.json;md5=1950748932ca316ec58675f625809860 \
                    file://node_modules/block-stream/package.json;md5=7b6a3c62899e98e27be3e9f68e778b91 \
                    file://node_modules/brace-expansion/package.json;md5=effd91994b1b7ddb8a33060ad4541e6a \
                    file://node_modules/caseless/package.json;md5=0b6c0d646d6be3e0439fd5c4cb50eb31 \
                    file://node_modules/chownr/package.json;md5=d01f1cdebdcc543145948a7070c8d012 \
                    file://node_modules/code-point-at/package.json;md5=4e85e8a583ebac72e2065d9e576c92cf \
                    file://node_modules/combined-stream/package.json;md5=089f95f5df2908028bdbbe545ad5c0e3 \
                    file://node_modules/concat-map/package.json;md5=85d8a674998927862b17adef4aa6a7b1 \
                    file://node_modules/console-control-strings/package.json;md5=501cc4421445f05407dd447de932f200 \
                    file://node_modules/core-util-is/package.json;md5=685b947d7cae341b1e6f305b205da7ae \
                    file://node_modules/dashdash/package.json;md5=0ffb1aad4da09fac3c64eb3c09d10c49 \
                    file://node_modules/debug/package.json;md5=ae814002086b04e3fb94c930eedfdb59 \
                    file://node_modules/deep-extend/package.json;md5=843d8bcf451f015c3a6b3930e0b6eaee \
                    file://node_modules/delayed-stream/package.json;md5=cd5e299bb3405995d7e81ead42d4949d \
                    file://node_modules/delegates/package.json;md5=fa4a364036777c0cf422bee58a0c4e2d \
                    file://node_modules/detect-libc/package.json;md5=32bbf729c7ec55592eda945fda7f36bf \
                    file://node_modules/ecc-jsbn/package.json;md5=0ee3b1b336a7d241844622b050681563 \
                    file://node_modules/extend/package.json;md5=5074ec873b4010df5203d20e73e65246 \
                    file://node_modules/extsprintf/package.json;md5=7c02b29c15d45f83ee155dc101993c76 \
                    file://node_modules/fast-deep-equal/package.json;md5=da1199afd6b2d7a1eaa9e30acd35f4e6 \
                    file://node_modules/fast-json-stable-stringify/package.json;md5=903a31da36d2259011d342196a9b1265 \
                    file://node_modules/forever-agent/package.json;md5=71ca2080c34c1c0f241895916e86b3a9 \
                    file://node_modules/form-data/package.json;md5=bd35e0b775988ee9080c108ad5aa2ee1 \
                    file://node_modules/fs-minipass/package.json;md5=8b4599fada2a56483e99b98b5348361a \
                    file://node_modules/fs.realpath/package.json;md5=3aa3d67ce378e330e293496dd3b9a506 \
                    file://node_modules/fstream/package.json;md5=b3f1aaa7913ab33da3fa06812782bfd7 \
                    file://node_modules/gauge/package.json;md5=ea229b587fee704faaf9431b96678324 \
                    file://node_modules/getpass/package.json;md5=37a5fcbc8b99a0676c8f83fc7169fe4a \
                    file://node_modules/glob/package.json;md5=7d0807efb6353ee0196cabbacbf210d7 \
                    file://node_modules/graceful-fs/package.json;md5=b31cf400f270df9d7173307ac050e826 \
                    file://node_modules/har-schema/package.json;md5=54d2c0fffb398fed596e246ee7a19829 \
                    file://node_modules/har-validator/package.json;md5=ed1b063932b04bc4dde19bd9293415de \
                    file://node_modules/has-unicode/package.json;md5=f14043c8a5d6df10d3671d83073d6883 \
                    file://node_modules/http-signature/package.json;md5=72db3ad4de36b5604debdf9e7bb78ff9 \
                    file://node_modules/iconv-lite/package.json;md5=a8b97f25878ddc5419a9afe173037035 \
                    file://node_modules/ignore-walk/package.json;md5=3c6476acb2be5ac3c88cdcca5b19623b \
                    file://node_modules/inflight/package.json;md5=85ba25624378c23e1ee9b33d3d103bf0 \
                    file://node_modules/inherits/package.json;md5=f73908dab55d4259f3ed052ce9fb2fbb \
                    file://node_modules/ini/package.json;md5=c817b5b8913b3ab535d215aad84c3a7f \
                    file://node_modules/is-fullwidth-code-point/package.json;md5=6bd5a684a5081f2d099cb800cf53ebd6 \
                    file://node_modules/is-typedarray/package.json;md5=018697ad65588671c2bdd7b3ec2bdef3 \
                    file://node_modules/isarray/package.json;md5=a490f11007b2cc9d19c4a250592c2e71 \
                    file://node_modules/isexe/package.json;md5=b7340828ee0e123814f9b855953de714 \
                    file://node_modules/isstream/package.json;md5=2c74d78c5b8e181e78e90b9a3e2fd0e0 \
                    file://node_modules/jsbn/package.json;md5=a10cd005c4727beb0ca9b4f9fb37b441 \
                    file://node_modules/json-schema/package.json;md5=52088871554be9185eff18969e96948a \
                    file://node_modules/json-schema-traverse/package.json;md5=e706b186146b3f00005442861f98c091 \
                    file://node_modules/json-stringify-safe/package.json;md5=184d1a71034cb154ad7d1abf0e64e3a0 \
                    file://node_modules/jsprim/package.json;md5=9e330579d00de959cda1726ea6aa37ef \
                    file://node_modules/mime-db/package.json;md5=a2d6a0ae45675ab34540644036bae28c \
                    file://node_modules/mime-types/package.json;md5=a6bb66d39adb0d570c3f285fc161e467 \
                    file://node_modules/minimatch/package.json;md5=b763d93b18d070a6449399d2e92d8c32 \
                    file://node_modules/minimist/package.json;md5=84505571ecc56b8071068f44de7c79b2 \
                    file://node_modules/minipass/package.json;md5=06016f1eb0e1b95822eabfc1b701b2e5 \
                    file://node_modules/minizlib/package.json;md5=aad8a7c127b3bd5bed2e9ddbe3800ee9 \
                    file://node_modules/mkdirp/package.json;md5=3b5ba3c4a04a8b0520bd0d392cf1c48f \
                    file://node_modules/ms/package.json;md5=b3ea7267a23f72028e774742792b114a \
                    file://node_modules/needle/package.json;md5=e29a735769636a89d70dcdb2adb8faab \
                    file://node_modules/node-addon-api/package.json;md5=ff1d670acc594aa5b8489e776e588c21 \
                    file://node_modules/node-gyp/package.json;md5=bf0ec04fbefe4b858131d2c488ed5c5f \
                    file://node_modules/node-pre-gyp/node_modules/nopt/package.json;md5=5192f3f2450d866a1118c12915c38e37 \
                    file://node_modules/node-pre-gyp/node_modules/tar/package.json;md5=352cdff8134633616c3ed6e32e7e9cf4 \
                    file://node_modules/node-pre-gyp/package.json;md5=2b9597f2ec85aa9bfb68c190fa922713 \
                    file://node_modules/nopt/package.json;md5=41ca7dc9f7e8f92e9acc64e258ff3dbb \
                    file://node_modules/npm-bundled/package.json;md5=c2498bcf2e7ad506ecb8302187285ebc \
                    file://node_modules/npm-normalize-package-bin/package.json;md5=8beb5d87634212035f1a5fe378e22fe4 \
                    file://node_modules/npm-packlist/package.json;md5=ab91377f5b194a17b33fb4ea03e5705c \
                    file://node_modules/npmlog/package.json;md5=5052e259f267dea1afa387baced17891 \
                    file://node_modules/number-is-nan/package.json;md5=2b5b5a279c98be57ebafdaa605a14584 \
                    file://node_modules/oauth-sign/package.json;md5=7b1e2f118776b8af50278f4629cba165 \
                    file://node_modules/object-assign/package.json;md5=2854c33ba575a9ebc613d1a617ece277 \
                    file://node_modules/once/package.json;md5=afb6ea3bdcad6397e11a71615bd06e3b \
                    file://node_modules/os-homedir/package.json;md5=cfc8a815cbc6e35583981f3006bfabd5 \
                    file://node_modules/os-tmpdir/package.json;md5=20555f37b3809082d0562ce7f144a04a \
                    file://node_modules/osenv/package.json;md5=4690af52479190b37d7e5df62fa167d8 \
                    file://node_modules/path-is-absolute/package.json;md5=ef6e018bdf67b82ab1285bc799b5367b \
                    file://node_modules/performance-now/package.json;md5=ffd06b287f3c72ca8ca1abcdf43aab53 \
                    file://node_modules/process-nextick-args/package.json;md5=6bd1fff965ff97b4aff54e6b4e382ed0 \
                    file://node_modules/psl/package.json;md5=53c8b1b2c58bc210351376f113307326 \
                    file://node_modules/punycode/package.json;md5=11e1d3e03bb34de07e247a480cebb0b0 \
                    file://node_modules/qs/package.json;md5=dabbbd0380e4fd44e4d49cceeda84119 \
                    file://node_modules/rc/package.json;md5=dcf8f74e9fad2b9d45a0c5d70eba335d \
                    file://node_modules/readable-stream/package.json;md5=55d646ab9e50735393b18c874d0bd5ab \
                    file://node_modules/request/package.json;md5=663c3e276e3ba0b503fc062d6ceb22e0 \
                    file://node_modules/rimraf/package.json;md5=c69fc8897c842f559f37a4e02455e90a \
                    file://node_modules/safe-buffer/package.json;md5=bd7ef6f38f0ba20882d2601bd3ecaf11 \
                    file://node_modules/safer-buffer/package.json;md5=274d956f400350c9f6cf96d22cdda227 \
                    file://node_modules/sax/package.json;md5=4f338f842e93421a35e3ec9e051d650b \
                    file://node_modules/semver/package.json;md5=332793fc37ed92b7e36a256af9ba52ff \
                    file://node_modules/set-blocking/package.json;md5=e37224b4c865b4464d6d41b1f8a870a4 \
                    file://node_modules/signal-exit/package.json;md5=37c7ccfe57d64fc58b6c352c0cd59083 \
                    file://node_modules/sshpk/package.json;md5=4ced8d334a4cd023e5b663c51ae88bc5 \
                    file://node_modules/string-width/package.json;md5=97dfb4a5985832f6a8c070ca4481b96c \
                    file://node_modules/string_decoder/package.json;md5=4a56e8c1789fe3bc13c55f8fec7e3ce2 \
                    file://node_modules/strip-ansi/package.json;md5=0b615dc471f5f434dd297c8655ed0c7f \
                    file://node_modules/strip-json-comments/package.json;md5=1dfad7430c94d2f136604def66ed9854 \
                    file://node_modules/tar/package.json;md5=d278ace652e28c583057153733d77899 \
                    file://node_modules/tough-cookie/package.json;md5=fd096f2790eb11bb538a93611a2820ee \
                    file://node_modules/tunnel-agent/package.json;md5=36d88acec2f39b7b86a347a0d8117296 \
                    file://node_modules/tweetnacl/package.json;md5=d8e646711b5775f632423c9ece45bec4 \
                    file://node_modules/uri-js/package.json;md5=dcc290a9747ad47e7edbcf7f25eb8a8d \
                    file://node_modules/util-deprecate/package.json;md5=73e6c3ff1709538c921d13a75cae485d \
                    file://node_modules/uuid/package.json;md5=a114f1fa334f6add6b4e4b8c0ca6057b \
                    file://node_modules/verror/package.json;md5=5721040ba86c8a3b64364ccd5576a208 \
                    file://node_modules/which/package.json;md5=9db3fcd8026f288ba3aaad83200f5a35 \
                    file://node_modules/wide-align/package.json;md5=b408a19d562c2232e6d09bd6fc51a4a9 \
                    file://node_modules/wrappy/package.json;md5=788804d507f3ed479ea7614fa7d3f1a5 \
                    file://node_modules/yallist/package.json;md5=cfcb34bc4394905533def53dbac8ad7a"

SRC_URI = " \
    git://github.com/mapbox/node-sqlite3.git;protocol=https \
    npmsw://${THISDIR}/${BPN}/npm-shrinkwrap.json \
    "

# Modify these as desired
PV = "5.0.0+git${SRCPV}"
SRCREV = "6e250c64199ce47ea8357a0a333f63ae0133e664"

S = "${WORKDIR}/git"
DEPENDS="python-native libuv"
inherit npm
RDEPENDS:${PN} += "bash"
LICENSE:${PN} = "Unknown"
LICENSE:${PN}-abbrev = "Unknown"
LICENSE:${PN}-ajv = "MIT"
LICENSE:${PN}-ansi-regex = "MIT"
LICENSE:${PN}-aproba = "ISC"
LICENSE:${PN}-are-we-there-yet = "ISC"
LICENSE:${PN}-asn1 = "Unknown"
LICENSE:${PN}-assert-plus = "Unknown"
LICENSE:${PN}-asynckit = "MIT"
LICENSE:${PN}-aws-sign2 = "Unknown"
LICENSE:${PN}-aws4 = "MIT"
LICENSE:${PN}-balanced-match = "Unknown"
LICENSE:${PN}-bcrypt-pbkdf = "Unknown"
LICENSE:${PN}-block-stream = "Unknown ISC"
LICENSE:${PN}-brace-expansion = "MIT"
LICENSE:${PN}-caseless = "Unknown"
LICENSE:${PN}-chownr = "ISC"
LICENSE:${PN}-code-point-at = "MIT"
LICENSE:${PN}-combined-stream = "MIT"
LICENSE:${PN}-concat-map = "MIT"
LICENSE:${PN}-console-control-strings = "ISC"
LICENSE:${PN}-core-util-is = "MIT"
LICENSE:${PN}-dashdash = "Unknown"
LICENSE:${PN}-debug = "MIT"
LICENSE:${PN}-deep-extend = "MIT"
LICENSE:${PN}-delayed-stream = "MIT"
LICENSE:${PN}-delegates = "MIT"
LICENSE:${PN}-detect-libc = "Unknown"
LICENSE:${PN}-ecc-jsbn = "Unknown MIT"
LICENSE:${PN}-extend = "MIT"
LICENSE:${PN}-extsprintf = "Unknown"
LICENSE:${PN}-fast-deep-equal = "MIT"
LICENSE:${PN}-fast-json-stable-stringify = "MIT"
LICENSE:${PN}-forever-agent = "Unknown"
LICENSE:${PN}-form-data = "MIT"
LICENSE:${PN}-fs-minipass = "ISC"
LICENSE:${PN}-fsrealpath = "Unknown"
LICENSE:${PN}-fstream = "ISC"
LICENSE:${PN}-gauge = "ISC"
LICENSE:${PN}-getpass = "MIT"
LICENSE:${PN}-glob = "Unknown"
LICENSE:${PN}-graceful-fs = "ISC"
LICENSE:${PN}-har-schema = "ISC"
LICENSE:${PN}-har-validator = "MIT"
LICENSE:${PN}-has-unicode = "ISC"
LICENSE:${PN}-http-signature = "MIT"
LICENSE:${PN}-iconv-lite = "MIT"
LICENSE:${PN}-ignore-walk = "ISC"
LICENSE:${PN}-inflight = "ISC"
LICENSE:${PN}-inherits = "ISC"
LICENSE:${PN}-ini = "ISC"
LICENSE:${PN}-is-fullwidth-code-point = "MIT"
LICENSE:${PN}-is-typedarray = "MIT"
LICENSE:${PN}-isarray = "Unknown"
LICENSE:${PN}-isexe = "ISC"
LICENSE:${PN}-isstream = "Unknown"
LICENSE:${PN}-jsbn = "Unknown"
LICENSE:${PN}-json-schema = "Unknown"
LICENSE:${PN}-json-schema-traverse = "MIT"
LICENSE:${PN}-json-stringify-safe = "ISC"
LICENSE:${PN}-jsprim = "Unknown"
LICENSE:${PN}-mime-db = "MIT"
LICENSE:${PN}-mime-types = "MIT"
LICENSE:${PN}-minimatch = "ISC"
LICENSE:${PN}-minimist = "MIT"
LICENSE:${PN}-minipass = "ISC"
LICENSE:${PN}-minizlib = "Unknown"
LICENSE:${PN}-mkdirp = "MIT"
LICENSE:${PN}-ms = "MIT"
LICENSE:${PN}-needle = "MIT"
LICENSE:${PN}-node-addon-api = "Unknown"
LICENSE:${PN}-node-gyp = "Unknown MIT"
LICENSE:${PN}-node-pre-gyp-nopt = "ISC"
LICENSE:${PN}-node-pre-gyp-tar = "ISC"
LICENSE:${PN}-node-pre-gyp = "Unknown"
LICENSE:${PN}-nopt = "ISC"
LICENSE:${PN}-npm-bundled = "ISC"
LICENSE:${PN}-npm-normalize-package-bin = "ISC"
LICENSE:${PN}-npm-packlist = "ISC"
LICENSE:${PN}-npmlog = "ISC"
LICENSE:${PN}-number-is-nan = "MIT"
LICENSE:${PN}-oauth-sign = "Unknown"
LICENSE:${PN}-object-assign = "MIT"
LICENSE:${PN}-once = "ISC"
LICENSE:${PN}-os-homedir = "MIT"
LICENSE:${PN}-os-tmpdir = "MIT"
LICENSE:${PN}-osenv = "ISC"
LICENSE:${PN}-path-is-absolute = "MIT"
LICENSE:${PN}-performance-now = "MIT"
LICENSE:${PN}-process-nextick-args = "Unknown"
LICENSE:${PN}-psl = "MIT"
LICENSE:${PN}-punycode = "MIT"
LICENSE:${PN}-qs = "Unknown"
LICENSE:${PN}-rc = "Unknown MIT"
LICENSE:${PN}-readable-stream = "Unknown"
LICENSE:${PN}-request = "Unknown"
LICENSE:${PN}-rimraf = "ISC"
LICENSE:${PN}-safe-buffer = "MIT"
LICENSE:${PN}-safer-buffer = "MIT"
LICENSE:${PN}-sax = "Unknown"
LICENSE:${PN}-semver = "ISC"
LICENSE:${PN}-set-blocking = "ISC"
LICENSE:${PN}-signal-exit = "ISC"
LICENSE:${PN}-sshpk = "MIT"
LICENSE:${PN}-string-width = "MIT"
LICENSE:${PN}-stringdecoder = "Unknown"
LICENSE:${PN}-strip-ansi = "MIT"
LICENSE:${PN}-strip-json-comments = "MIT"
LICENSE:${PN}-tar = "ISC"
LICENSE:${PN}-tough-cookie = "Unknown"
LICENSE:${PN}-tunnel-agent = "Unknown"
LICENSE:${PN}-tweetnacl = "Unknown"
LICENSE:${PN}-uri-js = "Unknown"
LICENSE:${PN}-util-deprecate = "MIT"
LICENSE:${PN}-uuid = "MIT"
LICENSE:${PN}-verror = "Unknown"
LICENSE:${PN}-which = "ISC"
LICENSE:${PN}-wide-align = "ISC"
LICENSE:${PN}-wrappy = "ISC"
LICENSE:${PN}-yallist = "ISC"
INSANE_SKIP:${PN} += " staticdev file-rdeps"
