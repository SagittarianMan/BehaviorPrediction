import jieba
import jieba.analyse
import pymysql
conn = pymysql.connect(host='localhost',user='root',
                        passwd='',db='FindX',port=3306,charset = 'utf8')
cur = conn.cursor()
cur.execute("use FindX")

def getTopWords(fileName):
    d={}
    content = open(file_name, 'rb').read()
    tags = jieba.analyse.extract_tags(content, topK=40)
    d[userId]=len(tags)
    print(",".join(tags))
    return tags

def temp():
    d = {1: 4, 2: 5, 3: 4, 4: 7, 5: 4, 6: 8, 7: 5, 8: 8, 9: 22, 10: 22}
    L=list(d)
    m=max(d,key=d.get)
    print(m)
    for i in L:
        print(str(d[i])+"\td")
        if d[i]==d[m]:
            print(i)
def getXs(uTags,otherUsers):##获取用户推荐结果及交集
    resultUsers=[]#算法得到的结果——一个用户ID集合（list对象）
    d={}#定义一个字典对象，保存userid和对应的交集的大小
    maxV=0#初始化交集的最大值
    for user in otherUsers:
          oTags=getTargetUserTags(user)#其他用户的兴趣词集合
          com=[tag for tag in oTags if tag in uTags]#取oTags和uTags的交集
          n = len(com)
          d[user]=com#保存在字典中
          
##    L=list(d)#键的list，元素是用户ID
    m=max(d,key=d.get)#值最大的第一个键
    maxV=d[m]#字典中最大的value值，即用户兴趣词交集的最大size
    print(m)
    for i in d:#L可以是d
         print(str(d[i])+"\td")
         if d[i]==maxV:
             print(i)
             resultUsers.append(i)
    return resultUsers,d             
#####################临时
def getXs2(uTags,otherUsers):
    resultUsers=[]#算法得到的结果——一个用户ID集合（list对象）
    d={}#定义一个字典对象，保存userid和对应的交集的大小
    max=0#初始化交集的最大值
    print(type(uTags))
    
    for user in otherUsers:
          oTags=getTargetUserTags(user)#其他用户的兴趣词集合
          print(type(oTags))
          print(oTags)
          com=[tag for tag in oTags if tag in uTags]#取oTags和uTags的交集
          print(com)
          n = len(com)
          d[user]=com#保存在字典中
          print(d)
    return d
              

def getResultWebsiteList(resultUsers,targetUser,d):
    resultWebsiteList=[]#保存符合推荐条件的历史记录的ID
    sql = "select id,words,visit_count from history_items where userid=%s  and web_title not in(select web_title from history_items where userid=%s)"
    for userid in resultUsers:
        count=cur.execute(sql,(userid,targetUser))
        #d[userid]#com 表示兴趣词交集
        com=d[userid]
        for i in range(0,count):
            item = cur.fetchone()
            wid = item[0]
            words=item[1]
            words=words.split('|')#字符串转为list
            visit = item[2]
            if visit>5:
                resultWebsiteList.append(wid)
                continue
            for w in words:
                if w in com:
                    resultWebsiteList.append(wid)
                    break
    return resultWebsiteList               
                
def recBySearchText(searchText):
    return
#temp()
##--------------------------------


def getOthersId(uid):##获取除uid以外的其他用户的ID
    sql = "select id from users where id != %s"
    count = cur.execute(sql,uid)
    others=[]
    for i in range(0,count):
        item=cur.fetchone()
        oid = item[0]
        others.append(oid)
    return others

def getTargetUserTags(uid):##get uTags
    sql = "select topwords from user_words where userid=%s"
    cur.execute(sql,uid)#一行记录
    item=cur.fetchone()
    topwords=item[0]
    topwords=topwords.split(',')
    return topwords

def test():
    others=getOthersId(23)
    uTags = getTargetUserTags(23)
    resultUsers,d=getXs(uTags,others)
    uid=23
    resultWebsiteList=getResultWebsiteList(resultUsers,uid,d)
def getd():
    d={}
    for i in range(0,5):
        d[i] = i+2
    return d
##d=getd()
##json.dumps(o, default=lambda o: o.__dict__, sort_keys=True, indent=4)
def test2():
    f=open("../1.txt",'rb')
    content=f.read().decode('gbk')
    
    print(content)

test2()    


