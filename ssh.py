import pexpect, pxssh, paramiko
import paramiko
import getpass

def sshToRemoteServer(host, username, password, cmd):
#pw = getpass.getpass()
    try:
 
        client = paramiko.SSHClient()
        client.set_missing_host_key_policy(paramiko.WarningPolicy())
        client.connect(host, port = 22, username = username, password = password)

        chan = client.get_transport().open_session()
        print "running '%s'" % cmd
        chan.exec_command(cmd)
        print "exit status: %s" % chan.recv_exit_status()
        if chan.recv_ready():
            print("recv:\n%s" % chan.recv(4096).decode('ascii'))
        if chan.recv_stderr_ready():
            print("error:\n%s" % chan.recv_stderr(4096).decode('ascii'))
        if chan.exit_status_ready():
            print("exit status: %s" % chan.recv_exit_status())
            key = False
            client.close()
        client.close()

    except Exception as e:
        print e
    


def scpToRemoteServer(password, command, child=None):
    '''
	Description:
      	 This method does the "SCP operation" in secure way.
	Input:
	 password of server ip and command.
	Return Type: Boolean
	Return Parameters : {status : 'Success/Failure', msg : 'OperationResult', data: 'Output'}
    '''
    try:
       expectations = ['[Pp]assword',
                       'continue (yes/no)?',
                       pexpect.EOF,
                       pexpect.TIMEOUT,
                       'Name or service not known',
                       'Permission denied',
                       'No such file or directory',
                       'No route to host',
                       'Network is unreachable',
                       'failure in name resolution',
                       'No space left on device'
                       ]
       if not child:
            child = pexpect.spawn(command )
       scpresult = child.expect( expectations )
       print "scp_result: ", scpresult
       if scpresult == 0:
            child.sendline(password)
            return scpToRemoteServer(password, command ,child)
       if scpresult == 1:
           child.sendline('yes')
           return scpToRemoteServer(password, command, child)
       if scpresult == 2:
          line = child.before
          print "Line -->", line
       if scpresult == 3:
          child.kill(0)
          return False
       if scpresult >= 4:
          child.kill(0)
          return False

       return True

    except:
       import traceback; traceback.print_exc()
       print "Did file finish?",child.exitstatus
       return False

