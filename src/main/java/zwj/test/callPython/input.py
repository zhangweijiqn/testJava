#coding=utf8
import sys
reload(sys)
sys.setdefaultencoding('utf8')

from chatterbot import ChatBot
from chatterbot.trainers import ChatterBotCorpusTrainer

deepThought = ChatBot("deepThought")
deepThought.set_trainer(ChatterBotCorpusTrainer)
# 使用中文语料库训练它
deepThought.train("chatterbot.corpus.chinese")  # 语料库
print(deepThought.get_response(u"很高兴认识你"))
print(deepThought.get_response(u"嗨，最近如何?"))
print(deepThought.get_response(u"复杂优于晦涩")) #语出 The Zen of Python
print(deepThought.get_response(u"面对模棱两可，拒绝猜测的诱惑."))
print(deepThought.get_response(u"你叫什么名字?"))
