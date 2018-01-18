# apkDecompileAndPackage
# 该项目指定对应的apk进行反编译，在指定的文件所在的正则匹配的位置插入一段代码
# 然后进行打包成一个APK


# decompile-apk
# 	先安装Apktool工具，在apktool目录下
#	安装如下步骤 进入网站https://ibotpeaches.github.io/Apktool/?utm_source=androiddevtools.cn&utm_medium=website
#	Windows:
#	Download Windows wrapper script (Right click, Save Link As apktool.bat)
#	Download apktool-2 (find newest here)
# 	Rename downloaded jar to apktool.jar
#	Move both files (apktool.jar & apktool.bat) to your Windows directory (Usually C://Windows)
#	If you do not have access to C://Windows, you may place the two files anywhere then add that directory to your Environment Variables System PATH variable.
#	Try running apktool via command prompt



#	准备apk文件以及sdk目录文件，放在当前目录下
# 	步骤（注意该decompile-apk文件夹要与对应的apk在同一个分区，否则可能引起操作失败）
#	一、修改配置文件
#		在当前目录下，修改cmd.properties
#		默认只需要修改两个参数即可
#		filePath以及storePass
#		执行apkinsert.bat批处理文件即可




#		要进行批量反编译打包的文件目录结构(参考example文件夹)
#			filePath
#			----apk
#				----*.apk		（存放所有的apk,必须以.apk结尾）
#			----com				（插码的所有内容）
#			----output			（打包后的apk文件夹以及签名后的apk）
#			----demo.keystore	（密匙库文件）
#
# 	二、然后进行打包成一个新的APK

# 	注意：中文乱码问题，请直接修改终端cmd的编码格式
