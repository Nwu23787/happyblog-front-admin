outFolder=$1
cd $outFolder
sleep 1
git add .
git commit -m'更新'
git push
