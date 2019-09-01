import pymysql
import jieba
import jieba.analyse

conn = pymysql.connect(host='localhost',user='root',
                        passwd='',db='FindX',port=3306,charset = 'utf8')
cur = conn.cursor()
cur.execute("use FindX")

##把每条记录的关键词分别写入用户文件中，方便提取用户兴趣词
def writeByUid(uid):
    cur1 = conn.cursor()
    cur1.execute("use FindX")
    file_name = str(uid)+'.txt'
    f=open(file_name,'w',encoding='UTF-8')
    uc=cur1.execute("select words from history_items where userid=%s",uid)
    for j in range(0,uc):
        item1 = cur1.fetchone()
        #print(item1[0])
        f.write(item1[0]+'\n')
    f.close()#把每条记录的关键词写入到文件中
    content = open(file_name, 'rb').read()
    tags = jieba.analyse.extract_tags(content, topK=40)#从文件中提取出词频最高的前40个词
    tags = (','.join(tags))
    print("tags:"+tags)
    cur2 = conn.cursor()
    cur2.execute("use FindX")
    cur2.execute("insert into user_words(userid,topwords) values(%s,%s)",(uid,tags))
    cur.connection.commit()
        
count = cur.execute("select userid from history_items group by userid")
print(count)
for i in range(0,count):
    print(i)
    item = cur.fetchone()
    print(item is None)##False
    print(item[0])
    uid = item[0]
    print(uid)
    writeByUid(uid)
