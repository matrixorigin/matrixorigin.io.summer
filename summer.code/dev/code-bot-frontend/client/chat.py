from typing import List

from domain.Message import Message
from utils.config import Config
import requests

GUIDE_CONVERSATION = 'I am code bot, how can I help you today? You can click add button to add the github repository you want to learn.'

config = Config()

class ChatClient:
    def get_groups(self, user_id):
        return ['default']

    def get_mes(self, user_id: str, group_name: str) -> List[Message]:
        return [Message(
            sender='bot',
            content=GUIDE_CONVERSATION
        )]

    def send_msg(self, name, group_name, text):
        return group_name + "%" + self.call_api(name, text)


    # get login code
    def send_code(self, code):
        try:
            response = requests.get(config.send_code_url + code)
            # Check if the request was successful
            if response.status_code == 200:
                # parse to json
                response_json = response.json()
                is_success = response_json['success']
                if is_success:
                    return response_json['data']
                else:
                    return None
            else:
                print(response)
                return None
        except Exception as e:
            print("An error occurred:", e)
            return None

    def call_api(self, name, text):
        try:
            url = config.chat_url
            data = {
                "partner_name": name,
                "chat_history": [],
                "content": text
            }
            response = requests.post(url, json=data)
            response_data = response.json()
            response_value = response_data.get("response")
            # print(response_value)
            return response_value
        except Exception as e:
            print(e)
        return 'error'

    def get_auth_url(self):
        try:
            response = requests.get(config.auth_url)
            # Check if the request was successful
            if response.status_code == 200:
                # parse to json
                response_json = response.json()
                # 提取 data 字段
                data_url = response_json['data']
                return data_url
            else:
                print(response)
                return None
        except Exception as e:
            print("An error occurred:", e)

    def subscribe(self, user_name, repo_name):
        url = config.subscribe_url

        payload = {
            "userName": f"{user_name}",
            "repoName": f"{repo_name}"
        }
        headers = {
            "Content-Type": "application/json"
        }

        # send the POST request
        response = requests.post(url, json=payload, headers=headers)

        if response.status_code == 200:
            print("Success:", response.json())
        else:
            print(f"Failed: Status code {response.status_code}, Response: {response.text}")

    def check_staus(self, repo_name):
        try:
            response = requests.get(config.check_status + f"/{repo_name}")
            # Check if the request was successful
            if response.status_code == 200:
                # parse to json
                response_json = response.json()
                # 提取 data 字段
                data_url = response_json['data']
                return data_url
            else:
                print(response)
                return None
        except Exception as e:
            print("An error occurred:", e)