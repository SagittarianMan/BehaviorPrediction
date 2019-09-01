import jieba
import jieba.analyse

file_name = "23.txt"
content = open(file_name, 'rb').read()
tags = jieba.analyse.extract_tags(content, topK=40)
print(",".join(tags))
