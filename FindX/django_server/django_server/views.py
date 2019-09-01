from django.http import HttpResponse
def test(request):
    f=open("1.txt",'rb')
    content=f.read().decode('gbk')
    
    print(content)
    return HttpResponse(content)
