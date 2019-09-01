import pymysql
import jieba
jieba.load_userdict('userdict.txt')
import jieba.analyse
from optparse import OptionParser
import string
import re

def cut(title):
    words=jieba.cut(title)
    words=list(words)
    print("|".join(words))

def insertW(iid,words):
    cur2 = conn.cursor()
    cur2.execute("update history_items set words=%s where id=%s",(words,iid))
    cur2.connection.commit()
    print('ok')
    

conn = pymysql.connect(host='localhost',user='root',
                        passwd='',db='FindX',port=3306,charset = 'utf8')
cur = conn.cursor()
cur.execute("use FindX")
count = cur.execute("select id,web_title,userid from history_items where web_title <>''")
print(count)

stopwords = {}.fromkeys([ line.rstrip() for line in open('stopword.txt') ])

for i in range(0,count):#不含上界
    item=cur.fetchone()#先把标点符号去掉再分词
    title = item[1]
    iid = item[0]
    uid = item[2]
    
    #title = title.replace(string.punctuation,' ')#只去掉了英文的标点符号\\//没用，错的
    #cut(title)
    #title = re.sub("[\s+\.\!\/_,$%^*(+\"\']+|[+——！，:\-。【】？、~@#￥%……&*（）]+",' ',title)#把英文字符去掉了
    #title = title.translate(string.punctuation)
    title = re.sub('[.\[\]\/\/:·,“”\"，「『』」\'。——|\-_？+【】»、!！()《》：；<\>（）?]',' ',title)
    #words=jieba.cut(item[1],cut_all=True)#不好
    words=jieba.cut(title)
    words=list(words)
        
    #print("|".join(words))#包含空格、标点符号、stopword
##    for word in words:
##        if word==' ':
##            words.remove(word)
##    if ' ' in words:
##        words.remove(' ')
    words = [word for word in words if word!=' ' and word not in stopwords]#去除list中的空格
    print("|".join(words))
    words = "|".join(words)
    if i==0:
        userid = uid
    else:
        if userid != uid:
            print(str(uid)+"\t"+str(userid))
            f.close()
            userid = uid
    
    f = open(str(userid)+'.txt','w',encoding='utf-8')
    f.write(words+"\n")
    insertW(iid,words)
f.close()
