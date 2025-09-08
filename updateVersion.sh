#! /bin/bash


# 检查是否提供了参数
if [ -z "$1" ]; then
    echo "错误：请提供新的版本号作为参数"
    echo "用法：$0 <新版本号>"
    exit 1
fi

# 去除版本号可能带有的v前缀（release-please生成的tag通常带v前缀）
new_version=$1
if [[ $new_version == v* ]]; then
    new_version=${new_version:1}
fi

# 检查版本号是否符合语义化版本格式（简单检查）
if ! [[ $new_version =~ ^[0-9]+\.[0-9]+\.[0-9]+(-[a-zA-Z0-9.]+)?$ ]]; then
    echo "错误：版本号格式不正确，应为语义化版本格式（如：1.0.0, 1.2.3-beta等）"
    exit 1
fi

# 获取当前版本号
old_version=$(cat version.txt)

# 显示更新信息
echo "当前版本: $old_version"
echo "更新到版本: $new_version"

# 更新version.txt文件
echo $new_version > version.txt
if [ $? -eq 0 ]; then
    echo "✓ version.txt 文件已更新"
else
    echo "✗ 更新version.txt文件失败"
    exit 1
fi

# 更新gradle.properties文件中的pluginVersion
sed -i '' "s/pluginVersion = .*/pluginVersion = $new_version/" gradle.properties
if [ $? -eq 0 ]; then
    echo "✓ gradle.properties 文件已更新"
else
    echo "✗ 更新gradle.properties文件失败"
    exit 1
fi

# 显示更新后的版本号
echo "\n更新完成！"
echo "version.txt: $(cat version.txt)"
echo "gradle.properties: $(grep pluginVersion gradle.properties)"